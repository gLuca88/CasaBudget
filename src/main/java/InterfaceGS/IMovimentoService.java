package InterfaceGS;

import java.util.List;

public interface IMovimentoService<T> {
	List<T> getMovimentiByUserId(int userId);
}
