package com.zfsmart.model;

import lombok.Data;

@Data
public class ArchiveTagEditCase {
    private int caseId;
    private String name;
    private String expected;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}
