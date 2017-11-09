package p000;

import org.cybergarage.soap.SOAP;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class lookup {
    public static void printAnswer(String name, Lookup lookup) {
        int i;
        System.out.print(new StringBuffer().append(name).append(SOAP.DELIM).toString());
        if (lookup.getResult() != 0) {
            System.out.print(new StringBuffer().append(" ").append(lookup.getErrorString()).toString());
        }
        System.out.println();
        Name[] aliases = lookup.getAliases();
        if (aliases.length > 0) {
            System.out.print("# aliases: ");
            for (i = 0; i < aliases.length; i++) {
                System.out.print(aliases[i]);
                if (i < aliases.length - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        if (lookup.getResult() == 0) {
            Record[] answers = lookup.getAnswers();
            for (Object println : answers) {
                System.out.println(println);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int type = 1;
        int start = 0;
        if (args.length > 2 && args[0].equals("-t")) {
            type = Type.value(args[1]);
            if (type < 0) {
                throw new IllegalArgumentException("invalid type");
            }
            start = 2;
        }
        for (int i = start; i < args.length; i++) {
            Lookup l = new Lookup(args[i], type);
            l.run();
            lookup.printAnswer(args[i], l);
        }
    }
}
