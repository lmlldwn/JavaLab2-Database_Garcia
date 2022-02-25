import java.time.LocalDateTime;
import java.util.List;

import static util.DateFormatterUtils.convertLocalDateTimeToString;

public class PromoManagerImpl implements PromoManager{
    private final DataAccess dataAccess = new DataAccess();

    @Override
    public void insertPromo(Promo promo) {
        dataAccess.insertPromo(promo);
    }

    @Override
    public boolean validateShortCode (String shortCode) {
        List<Promo> promoList = dataAccess.getPromoByShortCode(shortCode);
        return !promoList.isEmpty();
    }

    @Override
    public boolean validatePromo (String promoCode, String timeStamp) {
        List<Promo> promoList = dataAccess.getValidPromoByPromoCodeAndTimeStamp( promoCode,timeStamp);
        return !promoList.isEmpty();
    }

    @Override
    public List<Promo> retrieveAllPromo() {
        return dataAccess.getPromoData();
    }

    @Override
    public List<Promo> retrieveOngoingPromo() {
        return dataAccess.getValidPromoByTimeStamp(convertLocalDateTimeToString(LocalDateTime.now()));
    }
}
