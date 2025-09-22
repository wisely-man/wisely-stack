package io.github.wisely.web.i18n.plugins;

import com.wisely.framework.core.helper.StringHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;


@ConfigurationProperties(prefix = "plugins.i18n")
@Setter
@Getter
public class I18nProperties {

    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 国际化key
     */
    private String localeKey = "lang";
    /**
     * 本地化
     */
    private String locale = "zh_CN";
    /**
     * 编码
     */
    private String encoding = "UTF-8";
    /**
     * message路径，多个','分割
     */
    private String baseNames = "classpath*:i18n/messages";


    public Locale getI18nLocale() {
        if (StringHelper.isBlank(this.locale)) {
            return Locale.getDefault();
        }

        if (Strings.CS.indexOf(this.locale, "_") > -1) {
            String[] arr = StringHelper.split(this.locale, "_");
            return Locale.of(arr[0], arr[1]);
        }
        return Locale.of(this.locale);
    }

}
