package org.dcsa.api.validator.model;

import lombok.Data;
import org.dcsa.api.validator.constant.ValidationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class TestResult {
    String id;
    String name;
    String status;
    List<Map<ValidationType,String>> validations=new ArrayList<>();
}
