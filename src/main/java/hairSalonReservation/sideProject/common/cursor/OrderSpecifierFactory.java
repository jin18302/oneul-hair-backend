package hairSalonReservation.sideProject.common.cursor;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

public interface OrderSpecifierFactory<T extends EntityPath<?> , F extends SortableField> {

     OrderSpecifier[] generateOrderSpecifier(T t, F f, Order order);
}
