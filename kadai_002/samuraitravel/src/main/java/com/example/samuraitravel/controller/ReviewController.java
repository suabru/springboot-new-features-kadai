package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;


@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {
  private final ReviewRepository reviewRepository;
  private final HouseRepository houseRepository;
  private final ReviewService reviewService;
  
  public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository, ReviewService reviewService) {
	  this.reviewRepository = reviewRepository;
	  this.houseRepository = houseRepository;
	  this.reviewService = reviewService;
  }
  
  @GetMapping
  public String index(@PathVariable(name = "houseId") Integer houseId,  @PageableDefault(page = 0, size = 10, sort = "id")Pageable pageable, Model model) {
	  House house = houseRepository.getReferenceById(houseId);
      Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);
      
      model.addAttribute("reviewPage", reviewPage); 
      model.addAttribute("house", house);
      
      return "reviews/index";
  }
  
  @GetMapping("/register")
  public String register(@PathVariable(name = "houseId") Integer houseId, Model model) {
	  House house = houseRepository.getReferenceById(houseId);
	  
	  model.addAttribute("house", house);
	  model.addAttribute("reviewForm", new ReviewForm());
	  
	  return "reviews/register";
  }
  
  @PostMapping("/create")
  public String create(@PathVariable(name = "houseId") Integer houseId,
		               @AuthenticationPrincipal UserDetailsImpl userdetailsImpl,
		               @ModelAttribute @Validated ReviewForm reviewForm,
		               BindingResult bindingResult,
		               RedirectAttributes redirectAttributes,
		               Model model) {
	  House house = houseRepository.getReferenceById(houseId);
	  User user = userdetailsImpl.getUser();
	  
	  if(bindingResult.hasErrors()) {
		  model.addAttribute("house", house);
		  return "reviews/register";
	  }
	  
	  reviewService.create(house, user, reviewForm);
	  redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
	  
	  return "redirect:/houses/{houseId}";
  }
  
  @GetMapping("/{id}/edit")
  public String edit(@PathVariable(name = "id") Integer id, 
		             @PathVariable(name = "houseId") Integer houseId,
		             Model model) {
	  Review review = reviewRepository.getReferenceById(id);
	  House house = houseRepository.getReferenceById(houseId);
	  ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getScore(), review.getContent());
	  
	  model.addAttribute("review", review);
	  model.addAttribute("house", house);
	  model.addAttribute("reviewEditForm", reviewEditForm);
	  
	  return "reviews/edit";
	  
  }
  
  @PostMapping("/{id}/update")
  public String update(@PathVariable(name = "houseId") Integer houseId,
		               @PathVariable(name = "id") Integer id,
		               @ModelAttribute @Validated ReviewEditForm reviewEditForm,
		               BindingResult bindingResult,
		               RedirectAttributes redirectAttributes,
		               Model model) {
	  
	  House house = houseRepository.getReferenceById(houseId);
	  Review review = reviewRepository.getReferenceById(id);
	  
	  if(bindingResult.hasErrors()) {
		  model.addAttribute("house", house);
		  model.addAttribute("review", review);
		  return "reviews/edit";
	  }
	  
	  reviewService.update(reviewEditForm);
	  redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
	  
	  return "redirect:/houses/{houseId}";
  }
  
  @PostMapping("/{id}/delete")
  public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
	  reviewRepository.deleteById(id);
	  
	  redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
	  
	  return "redirect:/houses/{houseId}";
	  
	  }
  
}
