package lotto.controller;

import lotto.domain.Lotto;
import lotto.domain.Lottos;
import lotto.domain.LottoRank;
import lotto.domain.Money;
import lotto.view.LottoView;

import java.util.Map;

public class LottoMachine {

    private static LottoMachine lottoMachine;

    private LottoMachine() {}

    public void start(LottoView lottoView) {
        try {
            Money money = getMoney(lottoView);
            Lottos lottos = createLottoNumbers(lottoView, money);

            createLottoNumbers(lottoView, money);
            findWinningLotto(lottoView, money, lottos);
        } catch (NumberFormatException exception) {
            lottoView.showConvertNumberError();
            restart(lottoView);
        } catch (IllegalArgumentException exception) {
            lottoView.showErrorMessage(exception.getMessage());
            restart(lottoView);
        }
    }

    private void restart(LottoView lottoView) {
        lottoView.showRestartMessage();
        start(lottoView);
    }

    private Money getMoney(LottoView lottoView) {
       return new Money(lottoView.getMoney());
    }

    private Lottos createLottoNumbers(LottoView lottoView, Money money) {
        Lottos lottos = new Lottos(money.getLottoCount());
        lottoView.showLottoNumbers(lottos);

        return lottos;
    }

    private void findWinningLotto(LottoView lottoView, Money money, Lottos lottos) {
        // todo : 일급 컬렉션으로 감싸기
        Map<LottoRank, Long> rankGroup = lottos.getRankGroup(new Lotto(lottoView.getWinningLottoNumbers()));

        lottoView.showRankResult(rankGroup);
        lottoView.showProfitRate(money.getProfitRate(rankGroup));
    }

    public static LottoMachine getInstance() {
        if (lottoMachine == null) {
            synchronized (LottoMachine.class) {
                if (lottoMachine == null) {
                    lottoMachine = new LottoMachine();
                }
            }
        }

        return lottoMachine;
    }
}
