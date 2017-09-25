package com.eufh.drohne.controller;


import com.eufh.drohne.business.service.DroneService;
import com.eufh.drohne.business.service.ProcessedOrderService;
import com.eufh.drohne.business.service.TestService;
import com.eufh.drohne.business.service.impl.TestServiceImpl;
import com.eufh.drohne.domain.CsvOrder;
import com.eufh.drohne.domain.ProcessedOrder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
public class PartialController {

    private ProcessedOrderService processedOrderService;

    private TestService testService;

    private DroneService droneService;

    public PartialController(ProcessedOrderService processedOrderService, TestService testService, DroneService droneService) {
        this.processedOrderService = processedOrderService;
        this.testService = testService;
        this.droneService = droneService;
    }

    @RequestMapping("/partials/orders")
    public String getOrders(Model model) {
        ArrayList<ProcessedOrder> orders = this.processedOrderService.findAll();
        model.addAttribute("orders", orders);

        return "fragments/orders";
    }

    @RequestMapping(value ="/partials/addSampleOrders")
    public String addSampleOrders(Model model) {
        TestServiceImpl demo = new TestServiceImpl(null, testService, droneService, processedOrderService);
        demo.startWithDefaultOrders();

        ArrayList<ProcessedOrder> orders = this.processedOrderService.findAll();
        model.addAttribute("orders", orders);
        return "fragments/orders";
    }

    @RequestMapping(value ="/partials/addCsvOrders", method = RequestMethod.POST)
    public String addCsvOrders(@RequestBody CsvOrder[] csvOrders, Model model) {
        TestServiceImpl demo = new TestServiceImpl(null, testService, droneService, processedOrderService);
        demo.startWithCsvOrders(csvOrders);

        ArrayList<ProcessedOrder> orders = this.processedOrderService.findAll();
        model.addAttribute("orders", orders);
        return "fragments/orders";
    }

}
