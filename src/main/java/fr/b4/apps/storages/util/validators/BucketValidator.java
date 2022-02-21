package fr.b4.apps.storages.util.validators;

import fr.b4.apps.common.exceptions.BadRequestException;
import fr.b4.apps.storages.dto.BucketDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@UtilityClass
public class BucketValidator {
    public static void checkBucketDTOValidity(BucketDTO bucket) throws BadRequestException {
        if (!StringUtils.hasLength(bucket.getName())) {
            log.error("name cannot be empty");
            throw new BadRequestException("name cannot be empty");
        }
        if (ObjectUtils.isEmpty(bucket.getStorage()) || ObjectUtils.isEmpty(bucket.getStorage().getId())) {
            log.error("store (or store's id) is missing");
            throw new BadRequestException("store (or store's id) is missing");
        }
        if (ObjectUtils.isEmpty(bucket.getOwner()) || ObjectUtils.isEmpty(bucket.getOwner().getId())) {
            log.error("Owner (or Owner's id) is missing");
            throw new BadRequestException("Owner (or Owner's id) is missing");
        }
    }
}
