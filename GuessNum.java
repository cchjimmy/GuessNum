import java.util.Random;
import java.util.Scanner;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;

@FunctionalInterface
interface Verifier {
	boolean verify(String input);
}

class Reader {
	public Scanner s;

	public Reader(InputStream source) {
		s = new Scanner(source);
	}

	public String ask(String prompt, String invalidPrompt, Verifier v) {
		System.out.println(prompt);

		String input = s.nextLine();

		while (!v.verify(input)) {
			System.out.println(invalidPrompt);
			input = s.nextLine();
		}

		return input;
	}
}

public class GuessNum {
	public static void main(String[] args) {
		Verifier isInteger = (s) -> {
			try {
				new BigInteger(s);
				return true;
			} catch (Exception e) {
				return false;
			}
		};

		BigInteger max = new BigInteger("100");

		if (args.length > 0) {
			if (isInteger.verify(args[0])) {
				BigInteger arg = new BigInteger(args[0]);
				max = arg.compareTo(new BigInteger("-1")) == 1 ? arg : max;
			} else {
				System.out.println(
						"Usage: java GuessNum [max]\n	max - defaults to 100");
				return;
			}
		}

		boolean end = false;

		System.out.println("Welcome to guess a number!");

		Reader reader = new Reader(System.in);

		Random rng = new Random(new Date().getTime());

		while (!end) {
			BigInteger secret = new BigInteger(max.bitLength(), rng).mod(max);

			// System.out.println(String.format("The secret number is: %d", secret));

			System.out.println(String.format("A random number is generated between 0 to %d inclusively.",
					max));

			BigInteger input = new BigInteger(
					reader.ask(
							"Please enter a number:",
							"Invalid, please try again:",
							isInteger));

			int tries = 1;

			while (input.compareTo(secret) != 0) {
				System.out.println(input.compareTo(secret) == 1 ? "Too large" : "Too small.");

				input = new BigInteger(
						reader.ask(
								"Please enter another number:",
								"Invalid, please try again:",
								isInteger));

				tries++;
			}

			System.out.println(String.format("Congradulations! You guessed the right number in %d %s!",
					tries, tries == 1 ? "try" : "tries"));

			end = reader.ask("Enter 'n' to exit.", "", (s) -> true).equals("n");
		}
	}
}
