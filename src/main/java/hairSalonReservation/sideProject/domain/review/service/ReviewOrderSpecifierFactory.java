package hairSalonReservation.sideProject.domain.review.service;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import hairSalonReservation.sideProject.common.cursor.OrderSpecifierFactory;
import hairSalonReservation.sideProject.domain.review.entity.QReview;
import hairSalonReservation.sideProject.domain.review.entity.ReviewSortField;
import org.springframework.stereotype.Component;

@Component
public class ReviewOrderSpecifierFactory implements OrderSpecifierFactory<QReview, ReviewSortField> {

    @Override
    public OrderSpecifier[] generateOrderSpecifier(QReview review, ReviewSortField sortField, Order order) {

        Expression expression;
        switch (sortField) {
            case CREATED_AT -> expression = review.createdAt;
            case RATING -> expression = review.rating;
            default -> throw new RuntimeException();
        }

        return new OrderSpecifier[]{ new OrderSpecifier(order, expression), new OrderSpecifier(Order.DESC, review.id)};
    }
}
