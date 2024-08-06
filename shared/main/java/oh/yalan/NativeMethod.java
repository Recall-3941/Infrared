package oh.yalan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 娉ㄨВ鍒颁綘鎯宠娣锋穯鐨勬柟娉曚笂
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeMethod {
}
