package org.dcsa.api.provider.ctk.controller;

import org.dcsa.api.provider.ctk.init.AppProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Controller
public class BookingConfirmationController {

	private String officialBookingId;
/*	private CompletableFuture<String> completableFuture;
	public CompletableFuture<String> getBookingIdAsync(String bookingId) {
		completableFuture = new CompletableFuture<>();
		Executors.newCachedThreadPool().submit(() -> {
			Thread.sleep(500);
			completableFuture.complete(bookingId);
			return null;
		});
		return completableFuture;
	}*/

	@PostMapping("/officialBookingId")
	@ResponseBody
	public ResponseEntity<String> submitForm(@RequestParam("bookingId") String bookingId) throws ExecutionException, InterruptedException {
		System.out.println("Received bookingId: " + bookingId);
		officialBookingId = bookingId;
		return ResponseEntity.ok("Booking id:" +officialBookingId+ "is set successfully ");
	}
	@GetMapping("/bookingView")
	public String  getVide(Model model) {
		if (officialBookingId == null) {
			model.addAttribute("errorMessage1", "Booking ID is not submitted by official API yet!");
			model.addAttribute("errorMessage2", "Please wait Newman will set the booking ID when it runs the official booking API. It will show the booking ID.");
			return "errorView";
		}
		model.addAttribute("id", officialBookingId);
		int delayAmount = AppProperty.BOOKING_DELAY; // Delay amount in milliseconds
		model.addAttribute("delayAmount", delayAmount);
		return "bookingView";
	}

	@GetMapping("/bookingId")
	@ResponseBody
	public String getBookingId() {
		if(officialBookingId == null){
			return  "Booking ID is not submitted by official API yet!";
		}else{
			return officialBookingId;
		}
	}

	@PostMapping("/submitForm")
	@ResponseBody
	public ResponseEntity<String> submittedBookingId(@RequestParam("id") String id) throws InterruptedException {
		System.out.println("Submitted ID: " + id);
		Thread.sleep(AppProperty.BOOKING_DELAY);
		return ResponseEntity.ok("Delayed response after configured " + AppProperty.BOOKING_DELAY + " milliseconds. Booking ID submitted: " + id);
	}


}