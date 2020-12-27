import com.rpm.framework.spi.ServiceSupport;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;

/**
 * RPC框架对外提供的服务接口
 */
public interface RpcAccessPoint {
    /**
     * 客户端获取远程服务的引用
     * @param uri 远程服务地址
     * @param serviceClass 服务的接口类的Class
     * @param <T> 服务接口的类型
     * @return 远程服务引用
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);//和客户端的Reference注解一样，客户端调用这个方法可以获取远程服务的实例

    /**
     * 服务端注册服务的实现实例
     * @param service 实现实例
     * @param serviceClass 服务的接口类的Class
     * @param <T> 服务接口的类型
     * @return 服务地址
     */
    <T> URI addServiceProvider(T service, Class<T> serviceClass);//和dubbo的@Service注解一样，注册服务的实现

    default NameService getNameService(URI nameServiceUri) {
        Collection<NameService> nameServices = ServiceSupport.loadAll(NameService.class);
        for (NameService nameService:nameServices) {
            if (nameService.supportedSchemes().contains(nameServiceUri.getScheme())) {
                nameService.connect(nameServiceUri);
                return nameService;
            }
        }
        return null;
    }

    Closeable startServer() throws Exception;
}
