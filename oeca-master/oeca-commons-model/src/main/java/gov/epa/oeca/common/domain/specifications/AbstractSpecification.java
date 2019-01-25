package gov.epa.oeca.common.domain.specifications;

/**
 * @author dfladung
 * 
 * @param <T>
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

	public abstract boolean isSatisfiedBy(T t);

	public Specification<T> and(final Specification<T> specification) {
		return new AndSpecification<>(this, specification);
	}

	public Specification<T> or(final Specification<T> specification) {
		return new OrSpecification<>(this, specification);
	}

	public Specification<T> not(final Specification<T> specification) {
		return new NotSpecification<>(specification);
	}
}
