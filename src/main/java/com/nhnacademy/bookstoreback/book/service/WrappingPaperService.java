package com.nhnacademy.bookstoreback.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.book.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.book.repository.WrappingPaperRepository;

@Service
public class WrappingPaperService {
	@Autowired
	private WrappingPaperRepository wrappingPaperRepository;

	public WrappingPaper createWrappingPapers(WrappingPaper wrappingPaper) {
		return wrappingPaperRepository.save(wrappingPaper);
	}

	public WrappingPaper getWrappingPapers(Long id) {
		return wrappingPaperRepository.findById(id).orElse(null);
	}

	public WrappingPaper updateWrappingPapers(WrappingPaper wrappingPaper) {
		return wrappingPaperRepository.save(wrappingPaper);
	}

	public void deleteWrappingPapers(Long id) {
		wrappingPaperRepository.deleteById(id);
	}

	public List<WrappingPaper> getAllWrappingPapers() {
		return wrappingPaperRepository.findAll();
	}
}
