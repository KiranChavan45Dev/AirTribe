package com.libms.recommend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.libms.core.Branch;
import com.libms.model.Book;
import com.libms.model.Patron;

public class RecommendationService {

    private static final Logger logger = Logger.getLogger(RecommendationService.class.getName());
    
    public List<Book> recommend(Patron patron, Branch branch, int limit) {
        // TODO Auto-generated method stub
        Map<String, Integer> authorCount = new HashMap<>();

        for (String isbn : patron.getBorrowingHistory()) {
            branch.getInventory().getBook(isbn).ifPresent(b ->
                    authorCount.merge(b.getAuthor(), 1, Integer::sum));
        }

        if (authorCount.isEmpty()) {
            // fallback: newest available books
            return branch.getInventory().availableBooks().stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        List<String> favAuthors = authorCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Book> candidates = branch.getInventory().availableBooks().stream()
                .filter(b -> favAuthors.contains(b.getAuthor()))
                .filter(b -> !patron.getBorrowingHistory().contains(b.getIspn()))
                .limit(limit)
                .collect(Collectors.toList());
        logger.info("Recommended " + candidates.size() + " books for patron " + patron.getPatronId());
        return candidates;
    }
    
}
