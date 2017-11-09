package defpackage;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.xcrash.crashreporter.utils.CrashConst;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TTL;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Tokenizer.Token;
import org.xbill.DNS.Type;

public class update {
    int defaultClass = 1;
    long defaultTTL;
    PrintStream log = null;
    Message query;
    Resolver res;
    Message response;
    String server = null;
    Name zone = Name.root;

    void print(Object o) {
        System.out.println(o);
        if (this.log != null) {
            this.log.println(o);
        }
    }

    public Message newMessage() {
        Message msg = new Message();
        msg.getHeader().setOpcode(5);
        return msg;
    }

    public update(InputStream in) throws IOException {
        List<BufferedReader> inputs = new LinkedList();
        List istreams = new LinkedList();
        this.query = newMessage();
        inputs.add(new BufferedReader(new InputStreamReader(in)));
        istreams.add(in);
        while (true) {
            String line;
            do {
                try {
                    BufferedReader br = (BufferedReader) inputs.get(0);
                    if (((InputStream) istreams.get(0)) == System.in) {
                        System.out.print("> ");
                    }
                    line = br.readLine();
                    if (line == null) {
                        br.close();
                        inputs.remove(0);
                        istreams.remove(0);
                        if (inputs.isEmpty()) {
                            return;
                        }
                    }
                } catch (TextParseException tpe) {
                    System.out.println(tpe.getMessage());
                } catch (InterruptedIOException e) {
                    System.out.println("Operation timed out");
                } catch (SocketException e2) {
                    System.out.println("Socket error");
                } catch (IOException ioe) {
                    System.out.println(ioe);
                }
            } while (line == null);
            if (this.log != null) {
                this.log.println(new StringBuffer().append("> ").append(line).toString());
            }
            if (!(line.length() == 0 || line.charAt(0) == '#')) {
                if (line.charAt(0) == '>') {
                    line = line.substring(1);
                }
                Tokenizer tokenizer = new Tokenizer(line);
                Token token = tokenizer.get();
                if (token.isEOL()) {
                    continue;
                } else {
                    String operation = token.value;
                    if (operation.equals("server")) {
                        this.server = tokenizer.getString();
                        this.res = new SimpleResolver(this.server);
                        token = tokenizer.get();
                        if (token.isString()) {
                            this.res.setPort(Short.parseShort(token.value));
                        }
                    } else if (operation.equals(Album.KEY)) {
                        String keyname = tokenizer.getString();
                        String keydata = tokenizer.getString();
                        if (this.res == null) {
                            this.res = new SimpleResolver(this.server);
                        }
                        this.res.setTSIGKey(new TSIG(keyname, keydata));
                    } else if (operation.equals("edns")) {
                        if (this.res == null) {
                            this.res = new SimpleResolver(this.server);
                        }
                        this.res.setEDNS(tokenizer.getUInt16());
                    } else if (operation.equals("port")) {
                        if (this.res == null) {
                            this.res = new SimpleResolver(this.server);
                        }
                        this.res.setPort(tokenizer.getUInt16());
                    } else if (operation.equals("tcp")) {
                        if (this.res == null) {
                            this.res = new SimpleResolver(this.server);
                        }
                        this.res.setTCP(true);
                    } else if (operation.equals("class")) {
                        String classStr = tokenizer.getString();
                        int newClass = DClass.value(classStr);
                        if (newClass > 0) {
                            this.defaultClass = newClass;
                        } else {
                            print(new StringBuffer().append("Invalid class ").append(classStr).toString());
                        }
                    } else if (operation.equals("ttl")) {
                        this.defaultTTL = tokenizer.getTTL();
                    } else if (operation.equals("origin") || operation.equals("zone")) {
                        this.zone = tokenizer.getName(Name.root);
                    } else if (operation.equals("require")) {
                        doRequire(tokenizer);
                    } else if (operation.equals("prohibit")) {
                        doProhibit(tokenizer);
                    } else if (operation.equals("add")) {
                        doAdd(tokenizer);
                    } else if (operation.equals("delete")) {
                        doDelete(tokenizer);
                    } else if (operation.equals("glue")) {
                        doGlue(tokenizer);
                    } else if (operation.equals(SettingConstants.HELP) || operation.equals("?")) {
                        token = tokenizer.get();
                        if (token.isString()) {
                            update.help(token.value);
                        } else {
                            update.help(null);
                        }
                    } else if (operation.equals("echo")) {
                        print(line.substring(4).trim());
                    } else if (operation.equals("send")) {
                        sendUpdate();
                        this.query = newMessage();
                    } else if (operation.equals(DBColumns.IS_NEED_SHOW)) {
                        print(this.query);
                    } else if (operation.equals("clear")) {
                        this.query = newMessage();
                    } else if (operation.equals("query")) {
                        doQuery(tokenizer);
                    } else if (operation.equals("quit") || operation.equals("q")) {
                        if (this.log != null) {
                            this.log.close();
                        }
                        for (BufferedReader tbr : inputs) {
                            tbr.close();
                        }
                        System.exit(0);
                    } else if (operation.equals("file")) {
                        doFile(tokenizer, inputs, istreams);
                    } else if (operation.equals("log")) {
                        doLog(tokenizer);
                    } else if (operation.equals("assert")) {
                        if (!doAssert(tokenizer)) {
                            return;
                        }
                    } else if (operation.equals("sleep")) {
                        try {
                            Thread.sleep(tokenizer.getUInt32());
                        } catch (InterruptedException e3) {
                        }
                    } else if (operation.equals(CrashConst.KEY_ANR_DATE)) {
                        Date now = new Date();
                        token = tokenizer.get();
                        if (token.isString() && token.value.equals("-ms")) {
                            print(Long.toString(now.getTime()));
                        } else {
                            print(now);
                        }
                    } else {
                        print(new StringBuffer().append("invalid keyword: ").append(operation).toString());
                    }
                }
            }
        }
    }

    void sendUpdate() throws IOException {
        if (this.query.getHeader().getCount(2) == 0) {
            print("Empty update message.  Ignoring.");
            return;
        }
        if (this.query.getHeader().getCount(0) == 0) {
            Name updzone = this.zone;
            int dclass = this.defaultClass;
            if (updzone == null) {
                Record[] recs = this.query.getSectionArray(2);
                int i = 0;
                while (i < recs.length) {
                    if (updzone == null) {
                        updzone = new Name(recs[i].getName(), 1);
                    }
                    if (recs[i].getDClass() != 254 && recs[i].getDClass() != 255) {
                        dclass = recs[i].getDClass();
                        break;
                    }
                    i++;
                }
            }
            this.query.addRecord(Record.newRecord(updzone, 6, dclass), 0);
        }
        if (this.res == null) {
            this.res = new SimpleResolver(this.server);
        }
        this.response = this.res.send(this.query);
        print(this.response);
    }

    Record parseRR(Tokenizer st, int classValue, long TTLValue) throws IOException {
        long ttl;
        Name name = st.getName(this.zone);
        String s = st.getString();
        try {
            ttl = TTL.parseTTL(s);
            s = st.getString();
        } catch (NumberFormatException e) {
            ttl = TTLValue;
        }
        if (DClass.value(s) >= 0) {
            classValue = DClass.value(s);
            s = st.getString();
        }
        int type = Type.value(s);
        if (type < 0) {
            throw new IOException(new StringBuffer().append("Invalid type: ").append(s).toString());
        }
        Record record = Record.fromString(name, type, classValue, ttl, st, this.zone);
        if (record != null) {
            return record;
        }
        throw new IOException("Parse error");
    }

    void doRequire(Tokenizer st) throws IOException {
        Record record;
        Name name = st.getName(this.zone);
        Token token = st.get();
        if (token.isString()) {
            int type = Type.value(token.value);
            if (type < 0) {
                throw new IOException(new StringBuffer().append("Invalid type: ").append(token.value).toString());
            }
            boolean iseol = st.get().isEOL();
            st.unget();
            if (iseol) {
                record = Record.newRecord(name, type, 255, 0);
            } else {
                record = Record.fromString(name, type, this.defaultClass, 0, st, this.zone);
            }
        } else {
            record = Record.newRecord(name, 255, 255, 0);
        }
        this.query.addRecord(record, 1);
        print(record);
    }

    void doProhibit(Tokenizer st) throws IOException {
        int type;
        Name name = st.getName(this.zone);
        Token token = st.get();
        if (token.isString()) {
            type = Type.value(token.value);
            if (type < 0) {
                throw new IOException(new StringBuffer().append("Invalid type: ").append(token.value).toString());
            }
        }
        type = 255;
        Record record = Record.newRecord(name, type, 254, 0);
        this.query.addRecord(record, 1);
        print(record);
    }

    void doAdd(Tokenizer st) throws IOException {
        Record record = parseRR(st, this.defaultClass, this.defaultTTL);
        this.query.addRecord(record, 2);
        print(record);
    }

    void doDelete(Tokenizer st) throws IOException {
        Record record;
        Name name = st.getName(this.zone);
        Token token = st.get();
        if (token.isString()) {
            String s = token.value;
            if (DClass.value(s) >= 0) {
                s = st.getString();
            }
            int type = Type.value(s);
            if (type < 0) {
                throw new IOException(new StringBuffer().append("Invalid type: ").append(s).toString());
            }
            boolean iseol = st.get().isEOL();
            st.unget();
            if (iseol) {
                record = Record.newRecord(name, type, 255, 0);
            } else {
                record = Record.fromString(name, type, 254, 0, st, this.zone);
            }
        } else {
            record = Record.newRecord(name, 255, 255, 0);
        }
        this.query.addRecord(record, 2);
        print(record);
    }

    void doGlue(Tokenizer st) throws IOException {
        Record record = parseRR(st, this.defaultClass, this.defaultTTL);
        this.query.addRecord(record, 3);
        print(record);
    }

    void doQuery(Tokenizer st) throws IOException {
        int type = 1;
        int dclass = this.defaultClass;
        Name name = st.getName(this.zone);
        Token token = st.get();
        if (token.isString()) {
            type = Type.value(token.value);
            if (type < 0) {
                throw new IOException("Invalid type");
            }
            token = st.get();
            if (token.isString()) {
                dclass = DClass.value(token.value);
                if (dclass < 0) {
                    throw new IOException("Invalid class");
                }
            }
        }
        Message newQuery = Message.newQuery(Record.newRecord(name, type, dclass));
        if (this.res == null) {
            this.res = new SimpleResolver(this.server);
        }
        this.response = this.res.send(newQuery);
        print(this.response);
    }

    void doFile(Tokenizer st, List inputs, List istreams) throws IOException {
        String s = st.getString();
        try {
            InputStream is;
            if (s.equals("-")) {
                is = System.in;
            } else {
                is = new FileInputStream(s);
            }
            istreams.add(0, is);
            inputs.add(0, new BufferedReader(new InputStreamReader(is)));
        } catch (FileNotFoundException e) {
            print(new StringBuffer().append(s).append(" not found").toString());
        }
    }

    void doLog(Tokenizer st) throws IOException {
        String s = st.getString();
        try {
            this.log = new PrintStream(new FileOutputStream(s));
        } catch (Exception e) {
            print(new StringBuffer().append("Error opening ").append(s).toString());
        }
    }

    boolean doAssert(Tokenizer st) throws IOException {
        String field = st.getString();
        String expected = st.getString();
        String value = null;
        boolean flag = true;
        if (this.response == null) {
            print("No response has been received");
            return true;
        }
        if (field.equalsIgnoreCase("rcode")) {
            int rcode = this.response.getHeader().getRcode();
            if (rcode != Rcode.value(expected)) {
                value = Rcode.string(rcode);
                flag = false;
            }
        } else if (field.equalsIgnoreCase("serial")) {
            Record[] answers = this.response.getSectionArray(1);
            if (answers.length < 1 || !(answers[0] instanceof SOARecord)) {
                print("Invalid response (no SOA)");
            } else {
                long serial = answers[0].getSerial();
                if (serial != Long.parseLong(expected)) {
                    value = Long.toString(serial);
                    flag = false;
                }
            }
        } else if (field.equalsIgnoreCase("tsig")) {
            if (!this.response.isSigned()) {
                value = "unsigned";
            } else if (this.response.isVerified()) {
                value = ScreenSaverPingBack.SEAT_KEY_OK;
            } else {
                value = "failed";
            }
            if (!value.equalsIgnoreCase(expected)) {
                flag = false;
            }
        } else {
            int section = Section.value(field);
            if (section >= 0) {
                int count = this.response.getHeader().getCount(section);
                if (count != Integer.parseInt(expected)) {
                    value = new Integer(count).toString();
                    flag = false;
                }
            } else {
                print(new StringBuffer().append("Invalid assertion keyword: ").append(field).toString());
            }
        }
        if (!flag) {
            print(new StringBuffer().append("Expected ").append(field).append(" ").append(expected).append(", received ").append(value).toString());
            while (true) {
                Token token = st.get();
                if (!token.isString()) {
                    break;
                }
                print(token.value);
            }
            st.unget();
        }
        return flag;
    }

    static void help(String topic) {
        System.out.println();
        if (topic == null) {
            System.out.println("The following are supported commands:\nadd      assert   class    clear    date     delete\necho     edns     file     glue     help     key\nlog      port     prohibit query    quit     require\nsend     server   show     sleep    tcp      ttl\nzone     #\n");
            return;
        }
        topic = topic.toLowerCase();
        if (topic.equals("add")) {
            System.out.println("add <name> [ttl] [class] <type> <data>\n\nspecify a record to be added\n");
        } else if (topic.equals("assert")) {
            System.out.println("assert <field> <value> [msg]\n\nasserts that the value of the field in the last\nresponse matches the value specified.  If not,\nthe message is printed (if present) and the\nprogram exits.  The field may be any of <rcode>,\n<serial>, <tsig>, <qu>, <an>, <au>, or <ad>.\n");
        } else if (topic.equals("class")) {
            System.out.println("class <class>\n\nclass of the zone to be updated (default: IN)\n");
        } else if (topic.equals("clear")) {
            System.out.println("clear\n\nclears the current update packet\n");
        } else if (topic.equals(CrashConst.KEY_ANR_DATE)) {
            System.out.println("date [-ms]\n\nprints the current date and time in human readable\nformat or as the number of milliseconds since the\nepoch");
        } else if (topic.equals("delete")) {
            System.out.println("delete <name> [ttl] [class] <type> <data> \ndelete <name> <type> \ndelete <name>\n\nspecify a record or set to be deleted, or that\nall records at a name should be deleted\n");
        } else if (topic.equals("echo")) {
            System.out.println("echo <text>\n\nprints the text\n");
        } else if (topic.equals("edns")) {
            System.out.println("edns <level>\n\nEDNS level specified when sending messages\n");
        } else if (topic.equals("file")) {
            System.out.println("file <file>\n\nopens the specified file as the new input source\n(- represents stdin)\n");
        } else if (topic.equals("glue")) {
            System.out.println("glue <name> [ttl] [class] <type> <data>\n\nspecify an additional record\n");
        } else if (topic.equals(SettingConstants.HELP)) {
            System.out.println("help\nhelp [topic]\n\nprints a list of commands or help about a specific\ncommand\n");
        } else if (topic.equals(Album.KEY)) {
            System.out.println("key <name> <data>\n\nTSIG key used to sign messages\n");
        } else if (topic.equals("log")) {
            System.out.println("log <file>\n\nopens the specified file and uses it to log output\n");
        } else if (topic.equals("port")) {
            System.out.println("port <port>\n\nUDP/TCP port messages are sent to (default: 53)\n");
        } else if (topic.equals("prohibit")) {
            System.out.println("prohibit <name> <type> \nprohibit <name>\n\nrequire that a set or name is not present\n");
        } else if (topic.equals("query")) {
            System.out.println("query <name> [type [class]] \n\nissues a query\n");
        } else if (topic.equals("q") || topic.equals("quit")) {
            System.out.println("quit\n\nquits the program\n");
        } else if (topic.equals("require")) {
            System.out.println("require <name> [ttl] [class] <type> <data> \nrequire <name> <type> \nrequire <name>\n\nrequire that a record, set, or name is present\n");
        } else if (topic.equals("send")) {
            System.out.println("send\n\nsends and resets the current update packet\n");
        } else if (topic.equals("server")) {
            System.out.println("server <name> [port]\n\nserver that receives send updates/queries\n");
        } else if (topic.equals(DBColumns.IS_NEED_SHOW)) {
            System.out.println("show\n\nshows the current update packet\n");
        } else if (topic.equals("sleep")) {
            System.out.println("sleep <milliseconds>\n\npause for interval before next command\n");
        } else if (topic.equals("tcp")) {
            System.out.println("tcp\n\nTCP should be used to send all messages\n");
        } else if (topic.equals("ttl")) {
            System.out.println("ttl <ttl>\n\ndefault ttl of added records (default: 0)\n");
        } else if (topic.equals("zone") || topic.equals("origin")) {
            System.out.println("zone <zone>\n\nzone to update (default: .\n");
        } else if (topic.equals("#")) {
            System.out.println("# <text>\n\na comment\n");
        } else {
            System.out.println(new StringBuffer().append("Topic '").append(topic).append("' unrecognized\n").toString());
        }
    }

    public static void main(String[] args) throws IOException {
        InputStream in = null;
        if (args.length >= 1) {
            try {
                in = new FileInputStream(args[0]);
            } catch (FileNotFoundException e) {
                System.out.println(new StringBuffer().append(args[0]).append(" not found.").toString());
                System.exit(1);
            }
        } else {
            in = System.in;
        }
        update u = new update(in);
    }
}
