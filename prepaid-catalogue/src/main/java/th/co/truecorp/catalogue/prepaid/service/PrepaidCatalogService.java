/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package th.co.truecorp.catalogue.prepaid.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatOffer;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatOfferItem;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatOfferParam;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatOfferRelation;
import th.co.truecorp.catalogue.prepaid.jpa.entity.CatLogTable;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferItemRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferRelationRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferRepo;
import th.co.truecorp.catalogue.prepaid.kafka.dto.ChildOffer;
import th.co.truecorp.catalogue.prepaid.kafka.dto.Offer;
import th.co.truecorp.catalogue.prepaid.kafka.dto.OfferItem;
import th.co.truecorp.catalogue.prepaid.jpa.repo.OfferParamRepo;
import th.co.truecorp.catalogue.prepaid.jpa.repo.LogTableRepo;

/**
 *
 * @author Sorawe3
 */
@Service
public class PrepaidCatalogService {

    @Autowired
    private OfferRepo offerRepo;

    @Autowired
    private OfferItemRepo offerItemRepo;

    @Autowired
    private OfferParamRepo offerParamRepo;

    @Autowired
    private OfferRelationRepo offerRelationRepo;

    @Autowired
    private LogTableRepo logTableRepo;
    
    public void saveOfferPayload(Offer offer, Integer offset) throws Exception {

        if (offer == null) {
            throw new Exception("Offer data is null");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (offer.getSaleEffDate() != null) {
            offer.setSaleEffDate(formatter.parse(formatter.format(offer.getSaleEffDate())));
        }
        if (offer.getVersionEffDate() != null) {
            offer.setVersionEffDate(formatter.parse(formatter.format(offer.getVersionEffDate())));
        }

        CatOffer catOffer = CatOffer.builder()
                .id(offer.getId())
                .code(offer.getCode())
                .name(offer.getName())
                .description(offer.getDescription())
                .saleEffDate(offer.getSaleEffDate())
                .saleExpDate(offer.getSaleExpDate())
                .type(offer.getType())
                .productType(offer.getProductType())
                .saleContext(offer.getSaleContext())
                .versionEffDate(offer.getVersionEffDate())
                .versionExpDate(offer.getVersionExpDate())
                .offerProperties(parseOfferProperties(offer))
                .primaryParam(parsePrimaryParam(offer))
                .isDeployment("Y")
                .serviceLevel("C")
                .sysCreationDate(new Date())
                .build();

        List<CatOfferItem> catOfferItemList = new ArrayList<>();
        List<CatOfferParam> catOfferParamList = new ArrayList<>();

        List<CatLogTable> catLogWarningMsg = new ArrayList<>();

        List<OfferItem> offerItemList = Optional.ofNullable(offer)
                .map(Offer::getOfferItem).orElse(new ArrayList<>());
        for (OfferItem offerItem : offerItemList) {

            // offer item properties
            Map<String, String> offerItemProperties = Optional.ofNullable(offerItem)
                    .map(OfferItem::getOfferItemProperties).orElse(new HashMap<>());
            for (String properties : offerItemProperties.keySet()) {
                String value = "";
                if (properties.equalsIgnoreCase("Switch code")) {
                    if (offerItemProperties.get(properties).equalsIgnoreCase("")) {
                        value = null;
                        CatLogTable warningMsg = CatLogTable.builder()
                                .offerId(offset)
                                .packageId(0)
                                .pricingItemId(0)
                                .revenueType("W_TrueCAT - " + offer.getName())
                                .sysCreationDate(new Date())
                                .tmpRemark(offerItem.getName() + " - Switch code is null")
                                .build();
                        catLogWarningMsg.add(warningMsg);
                    } else {
                        value = "P";
                    }
                }
                CatOfferItem catOfferItem = CatOfferItem.builder()
                        .offerId(offer.getId())
                        .code(offer.getCode())
                        .itemId(offerItem.getId())
                        .itemCode(offerItem.getCode())
                        .name(offerItem.getName())
                        .description(offerItem.getDescription())
                        .sysCreationDate(new Date())
                        .itemProperties(parseOfferItemProperties(offerItem))
                        .itemType(value)
                        .build();
                catOfferItemList.add(catOfferItem);
            }

            // offer item param
            Map<String, String> offerParams = Optional.ofNullable(offerItem)
                    .map(OfferItem::getOfferItemParam).orElse(new HashMap<>());
            for (String paramName : offerParams.keySet()) {
                StringBuilder sb = new StringBuilder();
                if (offerParams.get(paramName).equalsIgnoreCase("")) {
                } else {
                    sb.append("defaultValue=").append(offerParams.get(paramName)).append(";");
                }
                CatOfferParam catOfferParam = CatOfferParam.builder()
                        .itemCode(offerItem.getCode())
                        .offerCode(offer.getCode())
                        .itemId(offerItem.getId())
                        .offerId(offer.getId())
                        .sysCreationDate(new Date())
                        .minimumValue(-1)
                        .maximumValue(-1)
                        .paramName(paramName)
                        .paramValue(sb.toString())
                        .build();
                catOfferParamList.add(catOfferParam);
            }
        }

        // offer relation
        List<CatOfferRelation> catOfferRelationList = new ArrayList<>();
        List<ChildOffer> childOfferList = Optional.ofNullable(offer)
                .map(Offer::getChildOffer).orElse(new ArrayList<>());
        for (ChildOffer childOffer : childOfferList) {
            CatOfferRelation catOfferRelation = CatOfferRelation.builder()
                    .parentId(offer.getId())
                    .childId(childOffer.getChildOfferId())
                    .parentCode(offer.getCode())
                    .childCode(childOffer.getCode())
                    .relationType(childOffer.getRelationType())
                    .selectedByDefault(parseSelectedByDefault(offer))
                    .sysCreationDate(new Date())
                    .build();
            catOfferRelationList.add(catOfferRelation);
        }

        offerRepo.save(catOffer);
        offerItemRepo.saveAll(catOfferItemList);
        offerParamRepo.saveAll(catOfferParamList);
        offerRelationRepo.saveAll(catOfferRelationList);
        logTableRepo.saveAll(catLogWarningMsg);
    }

    public void startProcess() {
        int uniqueId1 = ThreadLocalRandom.current().nextInt(10000000, 30000000);
        int uniqueId2 = ThreadLocalRandom.current().nextInt(10000000, 30000000);
        CatLogTable startProcess = CatLogTable.builder()
                .offerId(uniqueId1)
                .packageId(0)
                .pricingItemId(0)
                .sysCreationDate(new Date())
                .tmpRemark("PROCESS_CAT_FETCHALL")
                .build();
        logTableRepo.save(startProcess);
        CatLogTable processing = CatLogTable.builder()
                .offerId(uniqueId2)
                .packageId(0)
                .pricingItemId(0)
                .sysCreationDate(new Date())
                .tmpRemark("PROCESSING_CAT_FETCHALL")
                .build();
        logTableRepo.save(processing);
    }

    public void endProcess() {
        logTableRepo.deleteProcessingRemark();
        int uniqueId = ThreadLocalRandom.current().nextInt(10000000, 30000000);
        CatLogTable endProcess = CatLogTable.builder()
                .offerId(uniqueId)
                .packageId(0)
                .pricingItemId(0)
                .sysCreationDate(new Date())
                .tmpRemark("END_CAT_FETCHALL")
                .build();
        logTableRepo.save(endProcess);
    }

    // method to set primary param
    private String parsePrimaryParam(Offer offer) throws Exception {
        Map<String, String> properties = offer.getProperties();
        String value = "";
        for (String key : properties.keySet()) {
            if (key.equalsIgnoreCase("TR_CUSTOMER_TYPE")) {
                if (properties.get(key).equalsIgnoreCase("P") && offer.getType().equalsIgnoreCase("P")) {
                    value = "MSISDN";
                }
            }
        }
        return value;
    }

    // method to map offer properties with urNo and set nullable value
    private String parseOfferProperties(Offer offer) throws Exception {
        Map<String, String> properties = offer.getProperties();
        if (properties == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : properties.keySet()) {
            if (key.equalsIgnoreCase("TR_OFFER_GROUP")) {
                if (properties.get(key).equalsIgnoreCase("")) {
                    sb.append(key).append("=").append("NULL").append(";");
                }
            } else if (properties.get(key).equalsIgnoreCase("")) {
                sb.append(key).append("=").append("Null").append(";");
            } else {
                sb.append(key).append("=").append(properties.get(key)).append(";");
            }
        }
        if (offer.getUrNo().equalsIgnoreCase("")) {
        } else {
            sb.append("TR_UR_NO").append("=").append(offer.getUrNo()).append(";");
        }
        return sb.toString();
    }

    private String parseOfferItemProperties(OfferItem offerItem) throws Exception {
        StringBuilder sb = new StringBuilder();
        Map<String, String> offerItemProperties = Optional.ofNullable(offerItem)
                .map(OfferItem::getOfferItemProperties).orElse(new HashMap<>());
        for (String properties : offerItemProperties.keySet()) {
            sb.append(properties).append("=").append(offerItemProperties.get(properties)).append(";");
        }
        return sb.toString();
    }

    private String parseSelectedByDefault(Offer offer) throws Exception {
        String value = "";
        List<ChildOffer> childOfferList = Optional.ofNullable(offer)
                .map(Offer::getChildOffer).orElse(new ArrayList<>());
        for (ChildOffer childOffer : childOfferList) {
            if (childOffer.getSelectedByDefault() == true) {
                value = "Y";
            } else {
                value = "N";
            }
        }
        return value;
    }
}
