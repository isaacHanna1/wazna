package com.watad.controller;

import com.watad.services.CodeGeneratorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeGeneratorRestController {

    private final CodeGeneratorService codeGeneratorService;

    public CodeGeneratorRestController(CodeGeneratorService codeGeneratorService) {
        this.codeGeneratorService = codeGeneratorService;
    }

    @GetMapping("/api/generate-register-code")
    public String generateCode() {
        return codeGeneratorService.CodeGeneratorService();
    }
}
