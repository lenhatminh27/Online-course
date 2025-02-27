package com.course.common.utils;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils {

    public static boolean hasText(@Nullable String str) {
        return str != null && !str.isBlank();
    }

    public static String toSlug(String str) {
        String slug = deAccent(str);
        slug = slug.replaceAll("[:,\".><?';:{}\\[\\]]", "");
        slug = slug.replace(" ", "-").toLowerCase();
        return slug;
    }

    public static String uncapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }


    public static String deAccent(String str) {
        str = str.trim();
        str = str.replaceAll(" +", " ");
        str = str.replace("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replace("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replace("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replace("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replace("ì|í|ị|ỉ|ĩ", "i");
        str = str.replace("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replace("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replace("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replace("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replace("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replace("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replace("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replace("đ", "d");
        str = str.replace("Đ", "D");
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(nfdNormalizedString).replaceAll("");
        return str.toLowerCase();
    }

    public static String generateCustomId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generate(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        String input = "Đây là một chuỗi+// thử nghiệm: Làm thế nào để tạo slug?";
        String slug = toSlug(input);
        System.out.println(slug); // day-la-mot-chuoi-thu-nghiem-lam-the-nao-de-tao-slug
        System.out.println(deAccent("cao khả"));
    }
}



