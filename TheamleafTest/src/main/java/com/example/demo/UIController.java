package com.example.demo;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getConfig(@ModelAttribute("testDto") TestDto testDto) {
		ModelAndView model = new ModelAndView("test1");

		testDto = new TestDto();
		testDto.setName(testDto.getName() != null ? testDto.getName() : "Udara C Amarasinghe");
		testDto.setAge(testDto.getAge() != null ? testDto.getAge() : 31);

		model.addObject("testDto", testDto);

		return model;
	}

	@PostMapping(path = "/save")
	public ModelAndView save(@ModelAttribute("testDto") TestDto testDto, Model model) {
		ModelAndView modelView = new ModelAndView("test1");

		testDto.setStatus("SAVED");

		modelView.addObject("testDto", testDto);

		return modelView;
	}

	@PostMapping(value = "/jobapi/search/query")
	public String query(@RequestBody(required = false) SearchParams params, Model model) {
		List<Jobs> list = new ArrayList<>();
		list.add(new Jobs("Title1", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title3", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title4", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title5", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		list.add(new Jobs("Title2", "Location1", "Publiser1", "Company1"));
		
		Object object = new Object() {
			public List<Jobs> content = list;
			public Long totalElements = 2L;
		};

		model.addAttribute("jobs", object);
		return "test1 :: result-table";
	}
}
