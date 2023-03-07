package controller;

import domain.game.BlackjackGame;
import domain.game.GameResult;
import domain.strategy.RandomNumberGenerator;
import domain.user.People;
import domain.user.Player;
import view.InputView;
import view.OutputView;
import view.dto.PlayerDTO;
import view.mapper.GameResultMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlackjackController {

    private final InputView inputView;
    private final OutputView outputView;
    private BlackjackGame blackjackGame;

    public BlackjackController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void play() {
        initializeGame();
        blackjackGame.startHit();

        outputView.printPlayersInfoWhenGameStarted(
                PlayerDTO.from(blackjackGame.getPeople().getDealer()),
                makePlayersParameter(blackjackGame.getPeople()));

        blackjackGame.hitAllPlayersByCommand(
                inputView::inputCardCommand,
                outputView::printPlayerCardWithName);
        blackjackGame.letDealerHitUntilThreshold(outputView::printDealerHitMessage);

        outputView.printGameScore(
                PlayerDTO.of(blackjackGame.getPeople().getDealer(),
                        blackjackGame.getPeople().getDealer().sumHand()),
                makePlayersParameterWithResult(blackjackGame.getPeople())
        );

        outputView.printDealerRecord(PlayerDTO.from(blackjackGame.getPeople().getDealer()), makeDealerRecord());
        outputView.printPlayerRecord(makeAllPlayerRecordMap());
    }

    private Map<String, Integer> makeDealerRecord() {
        Map<String, Integer> dealerRecord = new HashMap<>();
        blackjackGame.getDealerRecord().forEach((key, value) -> dealerRecord.put(GameResultMapper.getGameResult(key), value));
        return dealerRecord;
    }

    private Map<String, String> makeAllPlayerRecordMap() {
        Map<Player, GameResult> gameResultMap = blackjackGame.getGameResultForAllPlayer();
        Map<String, String> strMap = new HashMap<>();
        gameResultMap.forEach((key, value) -> strMap.put(key.getPlayerName().getValue(), GameResultMapper.getGameResult(value)));
        return strMap;
    }

    private List<PlayerDTO> makePlayersParameterWithResult(People people) {
        return people.getPlayers().stream()
                .map(it -> PlayerDTO.of(it, it.sumHand()))
                .collect(Collectors.toList());
    }

    private List<PlayerDTO> makePlayersParameter(People people) {
        return people.getPlayers().stream()
                .map(PlayerDTO::from)
                .collect(Collectors.toList());
    }

    private void initializeGame() {
        List<String> playersName = inputView.inputParticipantsName();
        blackjackGame = new BlackjackGame(playersName, new RandomNumberGenerator());
    }

}
