import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Пример данных
        List<String> dataBlocks = List.of("SC1", "SC2", "SC3", "SC4");

        // Создаем дерево Меркла
        MerkleTree merkleTree = new MerkleTree();
        String rootHash = merkleTree.buildMerkleTree(dataBlocks);

        System.out.println("Корень дерева Меркла: " + rootHash);

        // Проверяем данные
        String dataToVerify = "SC2";
        List<String> proof = merkleTree.getProofForData(dataToVerify);
        boolean isValid = merkleTree.verifyProof(dataToVerify, proof, rootHash);
        System.out.println("Данные " + dataToVerify + " корректны: " + isValid);
    }

    private List<String> leaves = new ArrayList<>();
    private List<List<String>> levels = new ArrayList<>();

    // Построение дерева Меркла
    public String buildMerkleTree(List<String> dataBlocks) throws NoSuchAlgorithmException {
        // Хэшируем каждый блок данных и сохраняем их как листья
        for (String data : dataBlocks) {
            leaves.add(hash(data));
        }

        levels.add(new ArrayList<>(leaves)); // Добавляем листья как первый уровень

        // Создаем остальные уровни дерева
        while (levels.get(levels.size() - 1).size() > 1) {
            List<String> currentLevel = levels.get(levels.size() - 1);
            List<String> nextLevel = new ArrayList<>();

            for (int i = 0; i < currentLevel.size(); i += 2) {
                String left = currentLevel.get(i);
                String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left; // Если нет пары, дублируем
                nextLevel.add(hash(left + right));
            }

            levels.add(nextLevel);
        }

        // Последний уровень содержит корень
        return levels.get(levels.size() - 1).get(0);
    }

    // Получение пути для проверки данных
    public List<String> getProofForData(String data) throws NoSuchAlgorithmException {
        String hashedData = hash(data);
        List<String> proof = new ArrayList<>();
        int index = leaves.indexOf(hashedData);

        if (index == -1) {
            throw new IllegalArgumentException("Данные не найдены в дереве");
        }

        for (List<String> level : levels) {
            if (level.size() == 1) break; // Достигли корня дерева
            int pairIndex = (index % 2 == 0) ? index + 1 : index - 1; // Индекс пары
            if (pairIndex < level.size()) {
                proof.add(level.get(pairIndex));
            }
            index /= 2;
        }

        return proof;
    }

    // Проверка данных с использованием пути
    public boolean verifyProof(String data, List<String> proof, String rootHash) throws NoSuchAlgorithmException {
        String computedHash = hash(data);

        for (String siblingHash : proof) {
            if (computedHash.compareTo(siblingHash) < 0) {
                computedHash = hash(computedHash + siblingHash);
            } else {
                computedHash = hash(siblingHash + computedHash);
            }
        }

        return computedHash.equals(rootHash);
    }

    // Хэш-функция
    private String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
