package com.example.hospital.repository;

import com.example.hospital.model.Hospital;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

import java.util.Map;

@Repository
public class HospitalRepository extends InFileRepository<Hospital, String> {

    public HospitalRepository() {
        super("hospitals.json");
    }

    @Override
    protected Hospital convertToEntity(Object rawObject) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) rawObject;

        return new Hospital(
                (String) map.get("id"),
                (String) map.get("name"),
                (String) map.get("city"),
                null,
                null
        );
    }

    @Override
    protected String getEntityId(Hospital hospital) {
        return hospital.getId();
    }

    @Override
    protected void setEntityId(Hospital hospital, String id) {
        hospital.setId(id);
    }

    @Override
    protected String parseId(String id) {
        return id;
    }

    @Override
    protected Long parseIdToLong(String id) {
        if (id != null && id.startsWith("HOSP_")) {
            try {
                return Long.parseLong(id.substring(5));
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }
}
