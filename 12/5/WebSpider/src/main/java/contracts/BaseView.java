package contracts;

public interface BaseView<T extends BasePresenter> {
  void setPresenter(T presenter);
}
