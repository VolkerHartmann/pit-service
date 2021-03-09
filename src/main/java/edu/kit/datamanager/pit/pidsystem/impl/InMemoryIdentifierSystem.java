package edu.kit.datamanager.pit.pidsystem.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.kit.datamanager.pit.configuration.ApplicationProperties;
import edu.kit.datamanager.pit.domain.PIDRecord;
import edu.kit.datamanager.pit.domain.TypeDefinition;
import edu.kit.datamanager.pit.pidsystem.IIdentifierSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple basis for demonstrations or tests of the service. PIDs will be
 * stored in a HashMap and not stored anywhere else.
 */
public class InMemoryIdentifierSystem implements IIdentifierSystem {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryIdentifierSystem.class);
    private Map<String, PIDRecord> records;

    private ApplicationProperties applicationProperties;

    public InMemoryIdentifierSystem(ApplicationProperties appProperties) {
        this.records = new HashMap<>();
        this.applicationProperties = appProperties;
        LOG.warn("Using in-memory identifier system. REGISTERED PIDs ARE NOT STORED PERMANENTLY.");
    }

    @Override
    public boolean isIdentifierRegistered(String pid) throws IOException {
        return this.records.containsKey(pid);
    }

    @Override
    public PIDRecord queryAllProperties(String pid) throws IOException {
        return this.records.get(pid);
    }

    @Override
    public String queryProperty(String pid, TypeDefinition typeDefinition) throws IOException {
        return this.records.get(pid).getPropertyValue(typeDefinition.getIdentifier());
    }
    
    @Override
    public String registerPID(PIDRecord record) throws IOException {
        int counter = 0;
        do {
            int hash = record.getEntries().hashCode() + counter;
            record.setPid("tmp/test/" + hash);
            counter++;
        } while (this.records.containsKey(record.getPid()));
        this.records.put(record.getPid(), record);
        LOG.debug("Registered record with PID: {}", record.getPid());
        return record.getPid();
    }

    @Override
    public boolean updatePID(PIDRecord record) throws IOException {
        if (this.records.containsKey(record.getPid())) {
            this.records.put(record.getPid(), record);
            return true;
        }
        return false;
    }

    @Override
    public PIDRecord queryByType(String pid, TypeDefinition typeDefinition) throws IOException {
        PIDRecord allProps = this.queryAllProperties(pid);
        // only return properties listed in the type def
        Set<String> typeProps = typeDefinition.getAllProperties();
        PIDRecord result = new PIDRecord();
        for (String propID : allProps.getPropertyIdentifiers()) {
            if (typeProps.contains(propID)) {
                String[] values = allProps.getPropertyValues(propID);
                for (String value : values) {
                    result.addEntry(propID, "", value);
                }
            }
        }
        return result;
    }

    @Override
    public boolean deletePID(String pid) {
        throw new UnsupportedOperationException("Deleting PIDs is against the P in PID.");
    }
}