package com.dn.parking.apigateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;

@Schema(description = "Holds validation errors where the key is the field name")
public class ValidationResponse extends HashMap<String, String> {
}
