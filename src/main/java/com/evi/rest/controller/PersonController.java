package com.evi.rest.controller;

import com.evi.rest.model.Person;
import com.evi.rest.model.Selected;
import com.evi.rest.service.PersonFormValidator;
import com.evi.rest.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PersonController {
    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

    private PersonService personService;
    private PersonFormValidator personFormValidator;

    @Autowired
    @Qualifier(value="personService")
    public void setPersonService(PersonService ps) {
        this.personService = ps;
    }

    @Autowired
    @Qualifier(value="personFormValidator")
    public void setPersonFormValidator(PersonFormValidator personFormValidator) {
        this.personFormValidator = personFormValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(personFormValidator);
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String denied() {
        return "denied";
    }

    @RequestMapping(value = "/persons/list", method = RequestMethod.GET)
    public String getListAction(Model model) {
        model.addAttribute("selected", new Selected());
        model.addAttribute("person", new Person());
        model.addAttribute("listPersons", this.personService.listPersons());
        return "person";
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public String listPersons(Model model) {
        model.addAttribute("selected", new Selected());
        model.addAttribute("person", new Person());
        return "person";
    }

    @RequestMapping(value= "/person/add", method = RequestMethod.POST)
    public String addPerson(@ModelAttribute("person") @Validated Person p, BindingResult result) {
        if (result.hasErrors()) {
            LOG.info("Validation failed: "+result.getAllErrors().toString());
            return "person"; //returns view person
        }

        if(p.getId() == null) {
            personService.addPerson(p);
        } else {
            personService.updatePerson(p);
        }

        return "redirect:/persons/list";
    }

    @RequestMapping(value= "/persons/process", method = RequestMethod.POST)
    public ModelAndView dataProcessAction(@ModelAttribute("selected") Selected selected, Model model) {

        List<String> resultList = personService.processPersons(selected);
        model.addAttribute("resultList", resultList);

        ModelAndView model2 = new ModelAndView();
        model2.addObject("resultList", resultList);
        model2.setViewName("result");
        return model2;
    }

    @RequestMapping("/remove/{id}")
    public String removePerson(@PathVariable("id") int id) {
        personService.removePerson(id);
        return "redirect:/persons/list";
    }

    @RequestMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("selected", new Selected());
        model.addAttribute("person", personService.getPersonById(id));
        model.addAttribute("listPersons", personService.listPersons());
        return "person";
    }

    @RequestMapping(value = "/persons/result", method = RequestMethod.GET)
    public String listResult(Model model) {
        return "redirect:/persons";
    }

}
