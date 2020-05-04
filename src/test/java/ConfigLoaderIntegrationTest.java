import com.twitter.anubhav.ConfigLoader;
import com.twitter.anubhav.dto.Config;
import com.twitter.anubhav.parsers.GroupParser;
import com.twitter.anubhav.parsers.PropertyParser;
import com.twitter.anubhav.readers.ConfigFileReader;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigLoaderIntegrationTest {

    @Test
    void shouldLoadAllConfigWithActiveOverrides() {
        ConfigLoader configLoader = new ConfigLoader(new GroupParser(new PropertyParser()), new ConfigFileReader());
        Config config = configLoader.loadConfig(Objects.requireNonNull(this.getClass().getClassLoader().getResource("config.conf")).getPath(),
                Lists.list("ubuntu", "win"));

        assertThat(config.get("group1").get("prop")).isEqualTo("4");
        assertThat(config.get("group1").get("prop_one")).isEqualTo("abc");
        assertThat(config.get("group2").get("prop")).isEqualTo("6");
        assertThat(config.get("group2").get("prop_one")).isEqualTo("winv");
    }

}
