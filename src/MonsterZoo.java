import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.OptionalInt;

public class MonsterZoo {
	double distance = 0.0;// 歩いた距離
	int balls = 10;// モンスターを捕まえられるボールの数
	int fruits = 0;// ぶつけるとモンスターが捕まえやすくなるフルーツ

	// 卵は最大9個まで持てる．卵を取得するとeggにtrueが代入され，
	// 移動するたびに,eggDistanceに1.0kmずつ加算される．
	// 3km移動するとランダムでモンスターが孵る
	Double eggDistance[] = new Double[9];
	boolean egg[] = new boolean[9];

	// ユーザがGetしたモンスター一覧
	String userMonster[] = new String[100];

	// モンスター図鑑．モンスターの名前とレア度(0.0~9.0)がそれぞれの配列に保存されている
	// レア度が高いほうが捕まえにくい
	String monsterZukan[] = new String[22];
	double monsterRare[] = new double[22];

	public List<Integer> range_make(int listSize) {
		List<Integer> List_len = new ArrayList<>();
		IntStream.range(0, listSize)
				.forEach(i -> {
					List_len.add(i);
				});
		return List_len;
	}

	public void updateEggDistace() {
		IntStream.range(0, this.egg.length)
				.forEach(i -> {
					if (this.egg[i]) {
						this.eggDistance[i]++;
					}
				});
	}

	public void setANewEgg() {
		for (int i : range_make(this.eggDistance.length)) {// 卵は移動距離が進むと孵化するため，何km移動したかを更新する
			if (!this.egg[i]) {
				this.egg[i] = true;
				this.eggDistance[i] = 0.0;
				break;
			}
		}
	}

	public void foundZooStation() {
		System.out.println("ズーstationを見つけた！");
		int b = (int) (Math.random() * 3);// ball,fruits,eggがランダムに出る
		int f = (int) (Math.random() * 2);
		int e = (int) (Math.random() * 2);
		System.out.println("ボールを" + b + "個，" + "フルーツを" + f + "個" + "卵を" + e + "個Getした！");
		this.balls = this.balls + b;
		this.fruits = this.fruits + f;
		if (e >= 1) {// 卵を1つ以上Getしたら
			// egg[]に10個以上卵がない場合は新しい卵データをセットする
			setANewEgg();
		}
	}

	public double randomGenerator() {
		return this.monsterZukan.length * Math.random();
	}

	public int judgeThrowFruits(int ball_random) {
		if (this.fruits > 0) {
			System.out.println("フルーツを投げた！捕まえやすさが倍になる！");
			this.fruits--;
			ball_random = ball_random * 2;
		}
		return ball_random;
	}

	public void entryGotMonster(int monsterNum) {
		for (int j : range_make(userMonster.length)) {
			if (this.userMonster[j] == null) {
				this.userMonster[j] = this.monsterZukan[monsterNum];
				break;
			}
		}
	}

	public void foundMonster() {
		int m = (int) (randomGenerator());// monsterZukanからランダムにモンスターを出す
		System.out.println(this.monsterZukan[m] + "が現れた！");

		for (int i = 0; i < 3 && this.balls > 0; i++) {// 捕まえる or 3回ボールを投げるまで繰り返す
			int r = (int) (6 * Math.random());// 0~5までの数字をランダムに返す
			r = judgeThrowFruits(r);
			System.out.println(this.monsterZukan[m] + "にボールを投げた");
			this.balls--;
			if (this.monsterRare[m] <= r) {// monsterRare[m]の値がr以下の場合
				System.out.println(this.monsterZukan[m] + "を捕まえた！");
				entryGotMonster(m);
				break;// ボール投げ終了
			} else {
				System.out.println(this.monsterZukan[m] + "に逃げられた！");
			}
		}
	}


	public void eggHatched(int eggNum) {
		if (this.egg[eggNum] == true && this.eggDistance[eggNum] >= 3) {
			System.out.println("卵が孵った！");
			int m = (int) (randomGenerator());
			System.out.println(this.monsterZukan[m] + "が産まれた！");
			entryGotMonster(m);
			this.egg[eggNum] = false;
			this.eggDistance[eggNum] = 0.0;
		}
	}

	public void checkEggsHatched() {
		for (int i : range_make(this.egg.length)) {
			eggHatched(i);
		}
	}

	// 呼び出すと1km distanceが増える
	void move() {
		this.distance++;

		updateEggDistace();

		int flg1 = (int) (Math.random() * 10);// 0,1の場合はズーstation，7~9の場合はモンスター
		if (flg1 <= 1) {
			foundZooStation(); // ズーstation発見時の処理
		} else if (flg1 >= 7) {
			foundMonster(); // モンスター発見時の処理
		}
		checkEggsHatched();
	}
}
