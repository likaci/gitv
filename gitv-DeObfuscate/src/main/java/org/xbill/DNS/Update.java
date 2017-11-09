package org.xbill.DNS;

import java.io.IOException;
import java.util.Iterator;

public class Update extends Message {
    private int dclass;
    private Name origin;

    public Update(Name zone, int dclass) {
        if (zone.isAbsolute()) {
            DClass.check(dclass);
            getHeader().setOpcode(5);
            addRecord(Record.newRecord(zone, 6, 1), 0);
            this.origin = zone;
            this.dclass = dclass;
            return;
        }
        throw new RelativeNameException(zone);
    }

    public Update(Name zone) {
        this(zone, 1);
    }

    private void newPrereq(Record rec) {
        addRecord(rec, 1);
    }

    private void newUpdate(Record rec) {
        addRecord(rec, 2);
    }

    public void present(Name name) {
        newPrereq(Record.newRecord(name, 255, 255, 0));
    }

    public void present(Name name, int type) {
        newPrereq(Record.newRecord(name, type, 255, 0));
    }

    public void present(Name name, int type, String record) throws IOException {
        newPrereq(Record.fromString(name, type, this.dclass, 0, record, this.origin));
    }

    public void present(Name name, int type, Tokenizer tokenizer) throws IOException {
        newPrereq(Record.fromString(name, type, this.dclass, 0, tokenizer, this.origin));
    }

    public void present(Record record) {
        newPrereq(record);
    }

    public void absent(Name name) {
        newPrereq(Record.newRecord(name, 255, 254, 0));
    }

    public void absent(Name name, int type) {
        newPrereq(Record.newRecord(name, type, 254, 0));
    }

    public void add(Name name, int type, long ttl, String record) throws IOException {
        newUpdate(Record.fromString(name, type, this.dclass, ttl, record, this.origin));
    }

    public void add(Name name, int type, long ttl, Tokenizer tokenizer) throws IOException {
        newUpdate(Record.fromString(name, type, this.dclass, ttl, tokenizer, this.origin));
    }

    public void add(Record record) {
        newUpdate(record);
    }

    public void add(Record[] records) {
        for (Record add : records) {
            add(add);
        }
    }

    public void add(RRset rrset) {
        Iterator it = rrset.rrs();
        while (it.hasNext()) {
            add((Record) it.next());
        }
    }

    public void delete(Name name) {
        newUpdate(Record.newRecord(name, 255, 255, 0));
    }

    public void delete(Name name, int type) {
        newUpdate(Record.newRecord(name, type, 255, 0));
    }

    public void delete(Name name, int type, String record) throws IOException {
        newUpdate(Record.fromString(name, type, 254, 0, record, this.origin));
    }

    public void delete(Name name, int type, Tokenizer tokenizer) throws IOException {
        newUpdate(Record.fromString(name, type, 254, 0, tokenizer, this.origin));
    }

    public void delete(Record record) {
        newUpdate(record.withDClass(254, 0));
    }

    public void delete(Record[] records) {
        for (Record delete : records) {
            delete(delete);
        }
    }

    public void delete(RRset rrset) {
        Iterator it = rrset.rrs();
        while (it.hasNext()) {
            delete((Record) it.next());
        }
    }

    public void replace(Name name, int type, long ttl, String record) throws IOException {
        delete(name, type);
        add(name, type, ttl, record);
    }

    public void replace(Name name, int type, long ttl, Tokenizer tokenizer) throws IOException {
        delete(name, type);
        add(name, type, ttl, tokenizer);
    }

    public void replace(Record record) {
        delete(record.getName(), record.getType());
        add(record);
    }

    public void replace(Record[] records) {
        for (Record replace : records) {
            replace(replace);
        }
    }

    public void replace(RRset rrset) {
        delete(rrset.getName(), rrset.getType());
        Iterator it = rrset.rrs();
        while (it.hasNext()) {
            add((Record) it.next());
        }
    }
}
