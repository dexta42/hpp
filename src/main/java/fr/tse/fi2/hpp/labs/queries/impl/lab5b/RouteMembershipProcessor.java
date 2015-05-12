package fr.tse.fi2.hpp.labs.queries.impl.lab5b;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor {

    private final List<DebsRecord> records;

    public RouteMembershipProcessor(final QueryProcessorMeasure measure) {
        super(measure);
        this.records = new ArrayList<DebsRecord>();
    }

    @Override
    protected void process(final DebsRecord record) {
        this.records.add(record);
    }

    public boolean exists(final float pickLong, final float pickLat, final float dropLong, final float dropLat, final String license) {
        final Iterator<DebsRecord> it = this.records.iterator();

        while (it.hasNext()) {
            final DebsRecord record = it.next();
            if (record.getPickup_longitude() == pickLong && record.getPickup_latitude() == pickLat && record.getDropoff_longitude() == dropLong
                    && record.getDropoff_latitude() == dropLat && record.getHack_license().equals(license)) {
                return true;
            }
        }
        return false;
    }

    public DebsRecord getRandomRecord() {
        final Random rnd = new Random();
        if (rnd.nextBoolean()) {
            return new DebsRecord("", "", 0, 0, 0, 0, 0, 0, 0, 0, "", 0, 0, 0, 0, 0, 0, false);
        }
        return this.records.get(rnd.nextInt(this.records.size()));
    }
}