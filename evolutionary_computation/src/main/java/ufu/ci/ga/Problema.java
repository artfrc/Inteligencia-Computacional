package ufu.ci.ga;

import java.util.ArrayList;
import java.util.List;

// Um problema de criptoaritmetica do tipo "parcela1 + parcela2 = resultado".
// Guarda as letras distintas e oferece as duas funcoes de avaliacao do trabalho:
// erro global (diferenca absoluta entre a soma e o resultado) e erro posicional
// (soma das diferencas coluna a coluna da conta).
public class Problema {

    public static final int TOTAL_DIGITOS = 10;

    private final String parcela1;
    private final String parcela2;
    private final String resultado;

    private final char[] letras;          // letras distintas, na ordem de aparecimento
    private final int[] indicePorLetra;   // indexado por (letra - 'A'); -1 se a letra nao aparece
    private final boolean[] letraInicial; // marca, por indice de letra, se ela inicia alguma palavra

    public Problema(String parcela1, String parcela2, String resultado) {
        this.parcela1 = parcela1.toUpperCase();
        this.parcela2 = parcela2.toUpperCase();
        this.resultado = resultado.toUpperCase();

        this.indicePorLetra = new int[26];
        for (int i = 0; i < 26; i++) {
            indicePorLetra[i] = -1;
        }

        List<Character> distintas = new ArrayList<>();
        for (String palavra : new String[]{this.parcela1, this.parcela2, this.resultado}) {
            for (char c : palavra.toCharArray()) {
                if (indicePorLetra[c - 'A'] == -1) {
                    indicePorLetra[c - 'A'] = distintas.size();
                    distintas.add(c);
                }
            }
        }

        if (distintas.size() > TOTAL_DIGITOS) {
            throw new IllegalArgumentException(
                    "O problema possui mais de 10 letras distintas e nao cabe na representacao.");
        }

        this.letras = new char[distintas.size()];
        for (int i = 0; i < distintas.size(); i++) {
            letras[i] = distintas.get(i);
        }

        this.letraInicial = new boolean[distintas.size()];
        marcarLetraInicial(this.parcela1);
        marcarLetraInicial(this.parcela2);
        marcarLetraInicial(this.resultado);
    }

    private void marcarLetraInicial(String palavra) {
        char primeira = palavra.charAt(0);
        letraInicial[indicePorLetra[primeira - 'A']] = true;
    }

    public int getNumeroLetras() {
        return letras.length;
    }

    // Converte o cromossomo (digito -> indice de letra) no mapa inverso
    // (indice de letra -> digito), que e o formato usado pelas avaliacoes.
    public int[] decodificar(int[] cromossomo) {
        int[] digitoPorLetra = new int[letras.length];
        for (int digito = 0; digito < TOTAL_DIGITOS; digito++) {
            int indiceLetra = cromossomo[digito];
            if (indiceLetra < letras.length) {
                digitoPorLetra[indiceLetra] = digito;
            }
        }
        return digitoPorLetra;
    }

    public long erroGlobal(int[] digitoPorLetra) {
        long valor1 = valorPalavra(parcela1, digitoPorLetra);
        long valor2 = valorPalavra(parcela2, digitoPorLetra);
        long valorResultado = valorPalavra(resultado, digitoPorLetra);
        return Math.abs(valor1 + valor2 - valorResultado);
    }

    // Avalia a conta coluna a coluna, da direita para a esquerda, propagando o
    // "vai um". Em cada coluna soma os digitos das parcelas mais o transporte,
    // compara o digito de unidade com o digito esperado do resultado e acumula a
    // diferenca. O valor zera exatamente quando a conta esta correta.
    public long erroPosicional(int[] digitoPorLetra) {
        int colunas = Math.max(resultado.length(), Math.max(parcela1.length(), parcela2.length()));
        long erro = 0;
        int transporte = 0;
        for (int coluna = 0; coluna < colunas; coluna++) {
            int d1 = digitoNaColuna(parcela1, coluna, digitoPorLetra);
            int d2 = digitoNaColuna(parcela2, coluna, digitoPorLetra);
            int soma = d1 + d2 + transporte;
            int digitoCalculado = soma % 10;
            transporte = soma / 10;

            int digitoEsperado = digitoNaColuna(resultado, coluna, digitoPorLetra);
            erro += Math.abs(digitoCalculado - digitoEsperado);
        }
        erro += transporte; // se ainda sobra transporte, o resultado deveria ter mais um digito
        return erro;
    }

    public boolean temZeroAEsquerda(int[] digitoPorLetra) {
        for (int indice = 0; indice < letras.length; indice++) {
            if (letraInicial[indice] && digitoPorLetra[indice] == 0) {
                return true;
            }
        }
        return false;
    }

    // A execucao converge quando acha uma conta correta e sem zero a esquerda.
    public boolean ehSolucaoValida(int[] digitoPorLetra) {
        return erroGlobal(digitoPorLetra) == 0 && !temZeroAEsquerda(digitoPorLetra);
    }

    private long valorPalavra(String palavra, int[] digitoPorLetra) {
        long valor = 0;
        for (int i = 0; i < palavra.length(); i++) {
            int indiceLetra = indicePorLetra[palavra.charAt(i) - 'A'];
            valor = valor * 10 + digitoPorLetra[indiceLetra];
        }
        return valor;
    }

    // Digito da palavra na coluna indicada (0 = unidade); 0 se a coluna nao existe.
    private int digitoNaColuna(String palavra, int coluna, int[] digitoPorLetra) {
        int posicao = palavra.length() - 1 - coluna;
        if (posicao < 0) {
            return 0;
        }
        int indiceLetra = indicePorLetra[palavra.charAt(posicao) - 'A'];
        return digitoPorLetra[indiceLetra];
    }

    public String descricaoSolucao(int[] cromossomo) {
        int[] digitoPorLetra = decodificar(cromossomo);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letras.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(letras[i]).append('=').append(digitoPorLetra[i]);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return parcela1 + " + " + parcela2 + " = " + resultado;
    }
}
