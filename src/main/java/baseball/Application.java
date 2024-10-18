package baseball;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class InputSizeException extends Exception { };

class GameRestartEndException extends Exception { };

class Computer { // 문자열인지 아닌지 판단하는 메소드와 3개의 수를 뽑는 메소드를 가진 클래스
    public static boolean isNumeric(String s) { //문자열인지 아닌지 판단하는 메소드
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static void ComputerNumberSelect(List computer) { // 3개의 수를 뽑는 메소드
        while (computer.size() < 3) { // 컴퓨터가 임의의 수 3개 선택
            int randomNumber = Randoms.pickNumberInRange(1, 9);
            if (!computer.contains(randomNumber)) {
                computer.add(randomNumber);
            }
        }
    }
}

class Game {
    void GameStartMention() { // 게임 시작 문구 출력
        System.out.println("숫자 야구 게임을 시작합니다");
    }

    void GameIng(int gameRestartEnd, List user,List computer) throws InputSizeException, GameRestartEndException { //게임 진행 메소드
        while (gameRestartEnd == 1) { //1이면 진행, 2이면 종료
            //컴퓨터가 3개의 수 뽑기
            Computer.ComputerNumberSelect(computer);
            //게임 진행
            gameAlgorithm(user, computer);
            //게임을 다시 할것인지 묻기
            GameStratEndQuestion(gameRestartEnd);
        }
    }

    void gameAlgorithm(List user,List computer) throws InputSizeException {
        int ball = 0, strike = 0;
        do{
            String inputString = "";
            //입력 받기
            inputString = UserInput(inputString);
            //exception 확인
            StringDetermine(inputString);
            InputSizeDetermine(inputString);
            //리스트에 사용자 입력값 담기
            UserInputStorage(user, inputString);
            //strike와 ball 개수 확인하기
            CheckResult(user, computer,ball,strike);
            //확인 결과 알려주기
            GameResultMention(ball, strike);
        }while(strike!=3);
    }

    String UserInput(String inputString) { //문자열로 입력 받기
        System.out.print("숫자를 입력해주세요 : ");
        inputString = Console.readLine();
        return inputString;
    }

    void StringDetermine(String inputString) { //숫자가 입력되었는지 판별
        if (!Computer.isNumeric(inputString)) {
            throw new IllegalArgumentException(); //문자열이 입력되었을 때 발생하는 Exception
        }
    }

    void InputSizeDetermine(String inputString) throws InputSizeException { //숫자가 3자리수인지 판별
        if (inputString.length() != 3) {
            throw new InputSizeException();  //숫자가 3자리수가 아닐 때 발생하는 Exception
        }
    }

    void UserInputStorage(List user, String inputString) {  //입력받은 문자열을 숫자로 변환 후 배열에 저장
        int hundredNum, tenNum, oneNum, inputNumber;
        inputNumber = Integer.parseInt(inputString); //입력받은 문자열을 숫자로 변환
        hundredNum = inputNumber / 100; //100의 자리수 추출
        tenNum = (inputNumber - (100 * hundredNum)) / 10; //10의 자리수 추출
        oneNum = (inputNumber - (100 * hundredNum) - (10 * tenNum)); //1의 자리수 추출
        user.add(hundredNum); //user 배열에 넣기
        user.add(tenNum);
        user.add(oneNum);
    }

    void CheckResult (List user, List computer,int ball ,int strike) {
        for (int i=0; i<3;i++) {
            if (Objects.equals(user.get(i), computer.get(i))) {strike++;}
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Objects.equals(user.get(i), computer.get(j))) {ball++;}
            }
        }
        if (ball > 0 && ball > strike) {
             ball = ball - strike;
        }
    }

   void GameResultMention(int ball, int strike) {
       if (ball == 0 && strike == 0) { // 하나도 없는 경우
           System.out.println("낫싱");
       } else if (strike == 3) { // 전부 맞춘 경우
           System.out.println("3 스트라이크");
           System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임 종료");
       } else { // 볼과 스트라이크가 있는 경우
           System.out.println(ball + "볼 " + strike + "스트라이크");
       }
    }

    void GameStratEndQuestion(int gameRestartEnd) throws GameRestartEndException {
        System.out.print("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요");
        if (!(gameRestartEnd == 1 || gameRestartEnd == 2)) {
            throw new GameRestartEndException(); //1 또는 2가 아닐 때 발생하는 Exception
        } else {
            gameRestartEnd = Integer.parseInt(Console.readLine()); //2가 입력될 시 자동 종료
        }
    }
}

public class Application {
    public static void main(String[] args) {
        Computer c = new Computer();
        Game g = new Game();
        List<Integer> computer = new ArrayList<>();
        List<Integer> user = new ArrayList<>();

        int gameRestartEnd = 1; // 게임 진행 여부를 판단하기 위한 변수
        try {
            g.GameStartMention();
            g.GameIng(gameRestartEnd,user,computer);
        }catch (InputSizeException e) {
            System.out.println("3자리 숫자를 입력하세요.");
        }catch (GameRestartEndException e) {
            System.out.print("1 또는 2를 입력하세요");
        }

    }
}