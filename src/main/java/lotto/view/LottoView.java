package lotto.view;

import lotto.InputTool;
import lotto.domain.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LottoView {

    private static final String ERROR_MESSAGE_FORMAT = "오류가 발생했습니다. 원인 : %s";
    private static final String CONVERT_NUMBER_ERROR = "숫자만 입력이 가능합니다.";
    private static final String PURCHASE_QUESTION_TEXT = "구입금액을 입력해 주세요.";
    private static final String PURCHASE_RESULT_TEXT = "%s개를 구매했습니다.";
    private static final String WIN_QUESTION_TEXT = "지난 주 당첨 번호를 입력해 주세요.";
    private static final String WIN_RESULT_TEXT = "당첨통계\n----------";
    private static final String BONUS_NUMBER_QUESTION_TEXT = "보너스 번호를 입력해 주세요.";
    private static final String MATCH_RESULT_TEXT = "%d개 일치 (%d원)- %d개";
    private static final String PROFIT_RATE_RESULT_TEXT = "총 수익률은 %.2f 입니다.";
    private static final String RESTART_TEXT = "재시작...\n";
    private static final String LOTTO_NUMBER_FORMAT = "%s bonus : %d";
    private static final String LOTTO_NUMBER_PREFIX = "[";
    private static final String LOTTO_NUMBER_POSTFIX = "]";
    private static final String LOTTO_NUMBER_DELIMITER = ", ";

    private static LottoView lottoView;

    private final InputTool inputTool;

    private LottoView() {
        inputTool = new InputTool();
    }

    public int getMoney() {
        drawText(PURCHASE_QUESTION_TEXT);
        return inputTool.readLineToInt();
    }

    public List<Integer> getWinningNumbers() {
        newLine();
        drawText(WIN_QUESTION_TEXT);

        String[] lottoNumberTexts = inputTool.readLine().replace(" ", "").split(",");
        return textsToNumbers(lottoNumberTexts);
    }

    public int getWinningBonusNumber() {
        drawText(BONUS_NUMBER_QUESTION_TEXT);
        return inputTool.readLineToInt();
    }

    private List<Integer> textsToNumbers(String[] texts) {
        return Arrays.stream(texts)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    public void showLottoNumbers(Lottos lottos) {
        drawText(String.format(PURCHASE_RESULT_TEXT, lottos.size()));

        for (Lotto each : lottos.getValue()) {
            drawText(getNumbersText(each.getLottoNumber()));
        }
    }

    private String getNumbersText(LottoNumber lottoNumber) {

        String numbersText = lottoNumber.getNumbers()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(LOTTO_NUMBER_DELIMITER, LOTTO_NUMBER_PREFIX, LOTTO_NUMBER_POSTFIX));

        return String.format(LOTTO_NUMBER_FORMAT, numbersText, lottoNumber.getBonusNumber());
    }

    public void showRankResult(LottoRankGroup rankGroup) {
        newLine();
        drawText(WIN_RESULT_TEXT);
        showStatistics(rankGroup);
    }

    private void showStatistics(LottoRankGroup rankGroup) {
        Arrays.stream(LottoRank.values())
                .sorted(Comparator.reverseOrder())
                .forEach(rank -> {
                    Long rankCount = rankGroup.getValue().get(rank);
                    drawText(String.format(MATCH_RESULT_TEXT, rank.getMatchCount(), rank.getWinning(), rankCount == null ? 0 : rankCount));
                });
    }

    public void showProfitRate(double profitRate) {
        drawText(String.format(PROFIT_RATE_RESULT_TEXT, profitRate));
    }

    public void showConvertNumberError() {
        showErrorMessage(CONVERT_NUMBER_ERROR);
    }

    public void showErrorMessage(String message) {
        drawText(String.format(ERROR_MESSAGE_FORMAT, message));
    }

    public void showRestartMessage() {
        newLine();
        drawText(RESTART_TEXT);
    }

    private void drawText(String text) {
        System.out.println(text);
    }

    private void newLine() {
        drawText(System.lineSeparator());
    }

    public static LottoView getInstance() {
        if (lottoView == null) {
            synchronized (LottoView.class) {
                if (lottoView == null) {
                    lottoView = new LottoView();
                }
            }
        }

        return lottoView;
    }
}
