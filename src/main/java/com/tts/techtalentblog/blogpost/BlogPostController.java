package com.tts.techtalentblog.blogpost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BlogPostController {
	@Autowired
	private BlogPostRepository blogPostRepository;

	// temporary solution
	private static List<BlogPost> posts = new ArrayList<>();

	@GetMapping("/")
	public String index(Model model) {
		posts.removeAll(posts);
		for (BlogPost post : blogPostRepository.findAll()) {
			posts.add(post);
		}
		model.addAttribute("posts", posts);
		return "blogpost/index";
	}

	@GetMapping("/blogposts/new")
	public String newBlog(BlogPost blogPost) {
		return "blogpost/new";
	}

	@PostMapping("/blogposts")
	public String addNewBlogPost(BlogPost blogPost, Model model) {
		blogPostRepository.save(blogPost);
		model.addAttribute("blogPost", blogPost);
		return "blogpost/result";
	}

	@RequestMapping(value = "/blogposts/{id}", method = RequestMethod.GET)
	public String editPostWithId(@PathVariable Long id, Model model) {
		Optional<BlogPost> post = blogPostRepository.findById(id);

		if (post.isPresent()) {
			BlogPost actualPost = post.get();
			model.addAttribute("blogPost", actualPost);
		}
		return "blogpost/edit";
	}

	@RequestMapping(value = "/blogposts/update/{id}")
	public String updateExistingPost(@PathVariable Long id, BlogPost blogPost, Model model) {
		Optional<BlogPost> post = blogPostRepository.findById(id);
		if (post.isPresent()) {
			BlogPost actualPost = post.get();
			actualPost.setTitle(blogPost.getTitle());
			actualPost.setAuthor(blogPost.getAuthor());
			actualPost.setBlogEntry(blogPost.getBlogEntry());
			blogPostRepository.save(actualPost);
			model.addAttribute("blogPost", actualPost);
		}
		return "blogpost/result";
	}

	@RequestMapping(value = "/blogposts/delete/{id}")
	public String deletePostById(@PathVariable Long id, Model model) {
		Optional<BlogPost> post = blogPostRepository.findById(id);
		if (post.isPresent()) {
			blogPostRepository.deleteById(id);
			model.addAttribute("blogPost", post.get());
		}
		return "blogpost/delete";
	}

}
