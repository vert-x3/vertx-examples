// TODO: this file should be removed once core uses netty > 4.1.36
import com.oracle.svm.core.annotate.*;
import org.graalvm.nativeimage.*;

/**
 * This substitution allows the usage of platform specific code to do low level
 * buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.CleanerJava6")
final class Target_io_netty_util_internal_CleanerJava6 {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FieldOffset, declClassName = "java.nio.DirectByteBuffer", name = "cleaner")
    private static long CLEANER_FIELD_OFFSET;
}

/**
 * This substitution allows the usage of platform specific code to do low level
 * buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.PlatformDependent0")
final class Target_io_netty_util_internal_PlatformDependent0 {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FieldOffset, declClassName = "java.nio.Buffer", name = "address")
    private static long ADDRESS_FIELD_OFFSET;
}

@TargetClass(className = "io.netty.util.internal.PlatformDependent")
final class Target_io_netty_util_internal_PlatformDependent {
	/**
	 * The class PlatformDependent caches the byte array base offset by reading the
	 * field from PlatformDependent0. The automatic recomputation of Substrate VM
	 * correctly recomputes the field in PlatformDependent0, but since the caching
	 * in PlatformDependent happens during image building, the non-recomputed value
	 * is cached.
	 */
	@Alias
	@RecomputeFieldValue(kind = RecomputeFieldValue.Kind.ArrayBaseOffset, declClass = byte[].class)
	private static long BYTE_ARRAY_BASE_OFFSET;
}

/**
 * This substitution allows the usage of platform specific code to do low level
 * buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess")
final class Target_io_netty_util_internal_shaded_org_jctools_util_UnsafeRefArrayAccess {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.ArrayIndexShift, declClass = Object[].class)
    public static int REF_ELEMENT_SHIFT;
}

/**
 * This substitution forces the usage of the blocking DNS resolver
 */
@TargetClass(className = "io.vertx.core.spi.resolver.ResolverProvider")
final class Target_io_vertx_core_spi_resolver_ResolverProvider {

    @Substitute
    public static io.vertx.core.spi.resolver.ResolverProvider factory(io.vertx.core.Vertx vertx, io.vertx.core.dns.AddressResolverOptions options) {
        return new io.vertx.core.impl.resolver.DefaultResolverProvider();
    }
}
