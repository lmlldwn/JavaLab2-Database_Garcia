import java.util.List;

public interface PromoManager {
    void insertPromo(Promo promo);
    boolean validateShortCode(String shortCode);
    boolean validatePromo(String promoCode, String timeStamp);
    List<Promo> retrieveAllPromo();
    List<Promo> retrieveOngoingPromo();
}
