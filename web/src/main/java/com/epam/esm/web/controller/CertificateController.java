package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "")
    public String certificates(Model model) {
        model.addAttribute("certificateList", certificateService.getAll());
        return "certificates";
    }

    @GetMapping(value = "/{certificateId}")
    public String certificate(@PathVariable int certificateId, Model model) {
        model.addAttribute(certificateService.get(certificateId));
        return "certificate";
    }

    @GetMapping(value = "/create")
    public String showCreateForm() {
        return "create_certificate";
    }
}
