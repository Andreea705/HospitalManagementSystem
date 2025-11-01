package com.example.hospital.repository;
import com.example.hospital.model.Nurse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NurseRepo {

        private final List<Nurse> nurseList = new ArrayList<>();

        public Nurse save(Nurse nurse) {
            Nurse existing = findByQualificationLevel(nurse.getQualificationLevel());

            if (existing != null) {
                existing.setShift(nurse.getShift());
                existing.setOnDuty(nurse.isOnDuty());
                return existing;
            } else {
                nurseList.add(nurse);
                return nurse;
            }
        }

        public List<Nurse> findAllNurses() {
            return new ArrayList<>(nurseList);
        }

        public Nurse findByQualificationLevel(String qualificationLevel) {
            if (qualificationLevel == null) return null;

            for (Nurse nurse : nurseList) {
                if (nurse.getQualificationLevel().equals(qualificationLevel)) {
                    return nurse;
                }
            }
            return null;
        }

        public boolean deleteByQualificationLevel(String qualificationLevel) {
            Nurse nurse = findByQualificationLevel(qualificationLevel);
            if (nurse != null) {
                nurseList.remove(nurse);
                return true;
            }
            return false;
        }

        public boolean existsByQualificationLevel(String qualificationLevel) {
            return findByQualificationLevel(qualificationLevel) != null;
        }

        public List<Nurse> findByShift(String shift) {
            List<Nurse> result = new ArrayList<>();
            for (Nurse nurse : nurseList) {
                if (nurse != null && nurse.getShift().equals(shift)) {
                    result.add(nurse);
                }
            }
            return result;
        }

        public List<Nurse> findByOnDuty(boolean onDuty) {
            List<Nurse> result = new ArrayList<>();
            for (Nurse nurse : nurseList) {
                if (nurse != null && nurse.isOnDuty() == onDuty) {
                    result.add(nurse);
                }
            }
            return result;
        }
}

