package com.kongsonhawoo.friendkurly.service;

import com.kongsonhawoo.friendkurly.domain.Member;
import com.kongsonhawoo.friendkurly.domain.Product;
import com.kongsonhawoo.friendkurly.dto.ProductDto;
import com.kongsonhawoo.friendkurly.dto.RecommendRequestDto;
import com.kongsonhawoo.friendkurly.repository.MemberRepository;
import com.kongsonhawoo.friendkurly.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendService {

    @Value("${kurly.savedCsv.path}")
    private String savedCsvPath;

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 추천
     */
    public List<List<ProductDto>> recommendProduct(RecommendRequestDto recommendRequestDto) {

        List<List<ProductDto>> recommendProductList = new ArrayList<>();

        try {
            Optional<Member> member = memberRepository.findById(recommendRequestDto.getMember_id());

//            if (member == null) throw new "해당 회원이 없습니다."
//            if (member.get().getId() == 0 || member.get().getId() > 5) throw new "1~5범위의 회원ID를 입력해주세요."

            List<String> outputProductsStringList = getStringList(member);

            List<ProductDto> outputProductList = stringListToProductDtoList(outputProductsStringList);

            List<List<ProductDto>> selectProductList = new ArrayList<>();

            // 20개중 3개를 뽑는 경우의 수 1140개
            int[] combiArray = new int[3];
            combination(0, 0, recommendRequestDto.getBudget(), 3,
                    outputProductList, combiArray, selectProductList);

            // 20개중 2개를 뽑는 경우의 수 190개
            combiArray = new int[2];
            combination(0, 0, recommendRequestDto.getBudget(), 2,
                    outputProductList, combiArray, selectProductList);

            HashSet<ProductDto> duplicateCheckSet = new HashSet<>();

            getRecommendCandidate(recommendProductList, selectProductList, duplicateCheckSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recommendProductList;
    }

    /**
     * 선택 후보에서 랜덤으로 추천 후보 가져오기
     */
    private void getRecommendCandidate(List<List<ProductDto>> recommendProductList, List<List<ProductDto>> selectProductList,
                                       HashSet<ProductDto> duplicateCheckSet) {
//           if (selectProductList.size() == 0) throw new "예산이 너무 적습니다."

        Collections.shuffle(selectProductList);

        for (List<ProductDto> productDtoList : selectProductList) {
            if (recommendProductList.size() == 4) break;

            boolean alreadyRecommended = false;
            // 이미 추천된 상품이 포함되어 있으면 continue
            for (ProductDto productDto : productDtoList) {
                if (duplicateCheckSet.contains(productDto)) {
                    alreadyRecommended = true;
                    break;
                }
            }
            if (alreadyRecommended) continue;

            productDtoList.forEach(duplicateCheckSet::add);
            recommendProductList.add(productDtoList);
        }
    }

    /**
     * member의 추천 아이템 20개를 StringList로 가져오기
     */
    private List<String> getStringList(Optional<Member> member) {

        List<String> outputProductsStringList = new ArrayList<>();

        try {
            File csv = new File(savedCsvPath);
            BufferedReader br = new BufferedReader(new FileReader(csv));
            boolean skipFirstLine = true;
            String line = "";

            while ((line = br.readLine()) != null) {
                // 첫줄 skip
                if (skipFirstLine) {
                    skipFirstLine = false;
                    continue;
                }

                String[] column = line.split(",");
                Long memberId = Long.parseLong(column[0]) + 1;

                // 찾는멤버가 해당 멤버가 아니라면 continue
                if (memberId != member.get().getId()) continue;

                String[] products = column[1].substring(2, column[1].length()-2).replace("  ", " ").split(" ");

                outputProductsStringList = Arrays.asList(products);

                // 찾으면 break
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputProductsStringList;
    }

    /**
     * stringList To ProductDtoList
     */
    private List<ProductDto> stringListToProductDtoList(List<String> outputProductsStringList) {

        List<ProductDto> outputProductList = new ArrayList<>();

        try {
            for (String outputProduct : outputProductsStringList) {
                long id = Long.parseLong(outputProduct);
                Optional<Product> product = productRepository.findById(id);
                int price = (int) (Math.ceil((1 - product.get().getDiscount() / 100) * product.get().getStandardPrice()));
                outputProductList.add(new ProductDto(product.get().getId(), product.get().getProductName(), price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputProductList;
    }

    /**
     * DFS 조합 nCr
     */
    private void combination (int currentProductSize, int start, int budget, int selectProductSize,
                              List<ProductDto> outputProductList, int[] combiArray,
                              List<List<ProductDto>> selectProductList) {
        if (currentProductSize == selectProductSize) {
            checkBudget(budget, outputProductList, combiArray, selectProductList);
            return;
        }
        for (int i = start; i < outputProductList.size(); i++) {
            combiArray[currentProductSize] = i;
            combination(currentProductSize+1, i+1, budget, selectProductSize,
                    outputProductList, combiArray, selectProductList);
        }
    }

    /**
     * 조합의 예산 체크
     */
    private void checkBudget(int budget, List<ProductDto> outputProductList, int[] combiArray,
                             List<List<ProductDto>> selectProductList) {
        List<ProductDto> tmpProductList = new ArrayList<>();

        for (int idx : combiArray) {
            ProductDto productDto = outputProductList.get(idx);

            // 예산이 부족하면 return
            budget = budget - productDto.getPrice();
            if (budget < 0) return;

            tmpProductList.add(productDto);
        }
        Collections.shuffle(tmpProductList);
        selectProductList.add(tmpProductList);
    }
}