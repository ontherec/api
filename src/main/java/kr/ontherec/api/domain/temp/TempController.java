package kr.ontherec.api.domain.temp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class TempController {

	@GetMapping
	public String getSecured() {
		return "secured";
	}
}
