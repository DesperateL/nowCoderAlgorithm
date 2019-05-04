package supplement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Nim {

	public static void main(String[] args) {
		System.out.println("================================================================");
		System.out.println("该游戏最早由中国人发明，并由修铁路的华工传入西方");
		System.out.println("游戏假设有几堆硬币，两个人轮流拿硬币");
		System.out.println("规定1) 轮到某个人时只能选一个堆来拿硬币");
		System.out.println("规定2) 在这一堆上可以拿任意个，但是不可以不拿");
		System.out.println("规定3) 谁最后把硬币拿光谁赢");
		System.out.println("================================================================");
		System.out.println("游戏即将开始...任何时候想退出输入Q即可");
		System.out.println("================================================================");
		System.out.println("请输入游戏的格局，并以空格分割，每个数字请在0～十亿之间");
		System.out.println("比如你输入：1 2 3");
		System.out.println("代表一共有三堆硬币，第一堆1个硬币，第二堆2个硬币，第三堆3个硬币");
		System.out.println("请输入(并以回车结束)：");
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		if (input.equals("Q")) {
			sc.close();
			System.out.println("那下次再见啦～");
		} else {
			int[] arr = convertInputToArray(input);
			if (arr == null || arr.length == 0) {
				sc.close();
				System.out.println("不要瞎输入啊，你想明白再玩吧！再见！");
			} else {
				printArray(arr);
				if (isFinish(arr)) {
					sc.close();
					System.out.println("你选择的游戏格局是已经玩完的状态啦～～～那下次再见～～～");
					return;
				}
				System.out.println("希望先手还是后手？先手请输入1，后手请输入2");
				input = sc.nextLine();
				boolean nowUser = true;
				if (!input.equals("1") && !input.equals("2")) {
					sc.close();
					System.out.println("先手后手都整不明白！你想明白再玩吧！再见！");
					return;
				}
				if (input.equals("1")) {
					nowUser = true;
				} else {
					nowUser = false;
				}
				boolean posible = eorZero(arr) ^ nowUser;
				System.out.println("提示：目前选择下的这个开局，你" + (posible ? "有赢的可能" : "输定了"));
				System.out.println("在游戏过程中，轮到你时需要做出决定，比如想在第一堆上拿2个硬币，就输入:1 2");
				System.out.println("你决定了" + (nowUser ? "先手" : "后手") + ", 那我就是" + (!nowUser ? "先手" : "后手") + ", 游戏开始");
				if (nowUser) {
					if (!userOp(arr, sc)) {
						sc.close();
						System.out.println("那下次再见啦～");
						return;
					}
				}
				while (!isFinish(arr)) {
					winHand(arr);
					nowUser = false;
					if (isFinish(arr)) {
						break;
					}
					if (!userOp(arr, sc)) {
						sc.close();
						System.out.println("那下次再见啦～");
						return;
					}
					nowUser = true;
				}
				printArray(arr);
				sc.close();
				System.out.println("游戏结束, 你" + (nowUser ? "胜利了" : "失败了！"));
			}
		}
	}

	public static int[] convertInputToArray(String input) {
		String[] strs = input.split(" ");
		List<String> list = new ArrayList<>();
		for (String str : strs) {
			if (isNumber(str)) {
				list.add(str);
			}
		}
		int[] arr = new int[list.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Integer.valueOf(list.get(i));
		}
		return arr;
	}

	public static boolean isNumber(String s) {
		if (s == null || s.length() == 0) {
			return false;
		}
		char[] str = s.toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] < '0' || str[i] > '9') {
				return false;
			}
		}
		return true;
	}

	public static void printArray(int[] arr) {
		System.out.print("当前游戏格局： ");
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static boolean isValidOp(int index, int take, int[] arr) {
		if (index < 0 || index > arr.length - 1) {
			System.out.println("游戏格局里有第" + (index + 1) + "堆吗？咱别逗好吗？！");
			return false;
		}
		if (arr[index] == 0) {
			System.out.println("游戏格局里第" + (index + 1) + "堆已经空了，弟弟～～～");
			return false;
		}
		if (take < 1) {
			System.out.println("你可真优秀～只能拿走1个或1个以上的硬币哦～");
			return false;
		}
		if (take > arr[index]) {
			System.out.println("游戏格局里第" + (index + 1) + "堆一共只有" + arr[index] + "个硬币，");
			System.out.println("你要拿走" + take + "个？你这不是找乐儿吗～～～？");
			return false;
		}
		arr[index] -= take;
		return true;
	}

	public static boolean isFinish(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != 0) {
				return false;
			}
		}
		return true;
	}

	public static void winHand(int[] arr) {
		printArray(arr);
		int eor = 0;
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != 0) {
				eor ^= arr[i];
				list.add(i);
			}
		}
		int decideIndex = 0;
		int decideTake = 0;
		if (eor == 0) {
			decideIndex = list.get((int) (Math.random() * list.size()));
			decideTake = (int) (Math.random() * arr[decideIndex]) + 1;
		} else {
			List<Integer> candIndexs = new ArrayList<>();
			List<Integer> candTakes = new ArrayList<>();
			for (int index : list) {
				int eorExp = eor ^ arr[index];
				int take = arr[index] - eorExp;
				if (take > 0) {
					candIndexs.add(index);
					candTakes.add(take);
				}
			}
			int choose = (int) (Math.random() * candIndexs.size());
			decideIndex = candIndexs.get(choose);
			decideTake = candTakes.get(choose);
		}
		System.out.println("我的决定是在第" + (decideIndex + 1) + "堆上，拿走" + decideTake + "个硬币");
		arr[decideIndex] -= decideTake;
	}

	// false 想退出
	// true 正常进行
	public static boolean userOp(int[] arr, Scanner sc) {
		printArray(arr);
		boolean success = false;
		while (!success) {
			System.out.print("请你的输入决定:");
			String input = sc.nextLine();
			if (input.equals("Q")) {
				return false;
			}
			int[] op = convertInputToArray(input);
			if (op == null || op.length != 2) {
				System.out.println("请您认真输入，让我好好伺候您～");
				continue;
			}
			if (isValidOp(op[0] - 1, op[1], arr)) {
				System.out.println("你的决定是在第" + (op[0]) + "堆上，拿走" + op[1] + "个硬币");
				success = true;
			}
		}
		return true;
	}

	public static boolean eorZero(int[] arr) {
		int eor = 0;
		for (int num : arr) {
			eor ^= num;
		}
		return eor == 0;
	}

}
