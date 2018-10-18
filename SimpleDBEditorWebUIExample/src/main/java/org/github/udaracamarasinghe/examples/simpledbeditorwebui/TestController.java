package org.github.udaracamarasinghe.examples.simpledbeditorwebui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	public ResponseEntity<?> gettt(){
		return  ResponseEntity.ok("Test");
	}
}
