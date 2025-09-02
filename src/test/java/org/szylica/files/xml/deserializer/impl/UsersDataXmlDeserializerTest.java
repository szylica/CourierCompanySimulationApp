package org.szylica.files.xml.deserializer.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.szylica.config.AppTestBeansConfig;
import org.szylica.files.model.UsersData;
import org.szylica.files.xml.deserializer.XmlFileDeserializer;

import java.nio.file.Paths;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class UsersDataXmlDeserializerTest {

    @Autowired
    private XmlFileDeserializer<UsersData> xmlFileDeserializer;

    @Test
    @DisplayName("When xml data is loaded correctly")
    void test1(){
        var path = Paths.get("src", "test", "resources", "users-test.xml");
        var users = xmlFileDeserializer.deserializeFromFile(path);

        Assertions.assertThat(users.getUsers()).hasSize(5);

    }

    @Test
    @DisplayName("When xml deserialization works")
    void test2(){
        var xml = """
                <users>
                    <user>
                        <id>1</id>
                        <firstName>Jan</firstName>
                        <lastName>Kowalski</lastName>
                        <email>jan.kowalski@example.com</email>
                        <phone>+48 600 100 200</phone>
                        <latitude>52.2297</latitude>
                        <longitude>21.0122</longitude>
                    </user>
                    <user>
                        <id>2</id>
                        <firstName>Anna</firstName>
                        <lastName>Nowak</lastName>
                        <email>anna.nowak@example.com</email>
                        <phone>+48 601 200 300</phone>
                        <latitude>50.0647</latitude>
                        <longitude>19.9450</longitude>
                    </user>
                    <user>
                        <id>3</id>
                        <firstName>Piotr</firstName>
                        <lastName>Wiśniewski</lastName>
                        <email>piotr.wisniewski@example.com</email>
                        <phone>+48 602 300 400</phone>
                        <latitude>54.3520</latitude>
                        <longitude>18.6466</longitude>
                    </user>
                    <user>
                        <id>4</id>
                        <firstName>Katarzyna</firstName>
                        <lastName>Wójcik</lastName>
                        <email>katarzyna.wojcik@example.com</email>
                        <phone>+48 603 400 500</phone>
                        <latitude>51.1079</latitude>
                        <longitude>17.0385</longitude>
                    </user>
                    <user>
                        <id>5</id>
                        <firstName>Marek</firstName>
                        <lastName>Lewandowski</lastName>
                        <email>marek.lewandowski@example.com</email>
                        <phone>+48 604 500 600</phone>
                        <latitude>53.1325</latitude>
                        <longitude>23.1688</longitude>
                    </user>
                </users>
                """;

        var usersData = xmlFileDeserializer.deserialize(xml);
        Assertions.assertThat(usersData.getUsers()).hasSize(5);

    }

    @Test
    @DisplayName("When xml is malformed")
    void test3(){
        var xml = """
                <users>
                    <user>
                        <id>1</id>
                        <firstName>Jan</firstName>
                        <lastName>Kowalski</lastName>
                        <email>jan.kowalski@example.com</email>
                        <phone>+48 600 100 200</phone>
                        <latitude>52.2297</latitude>
                        <longitude>21.0122</longitude>
                    </user>
                    <user>
                        <id>2</id>
                        <firstName>Anna</firstName>
                        <lastName>Nowak</lastName>
                        <email>anna.nowak@example.com</email>
                        <phone>+48 601 200 300</phone>
                        <latitude>50.0647</latitude>
                        <longitude>19.9450</longitude>
                    </user>
                    <user>
                        <id>3</id>
                        <firstName>Piotr</firstName>
                        <lastName>Wiśniewski</lastName>
                        <email>piotr.wisniewski@example.com</email>
                        <phone>+48 602 300 400</phone>
                        <latitude>54.3520</latitude>
                        <longitude>18.6466</longitude>
                    </user>
                    <user>
                        <id>4</id>
                        <firstName>Katarzyna</firstName>
                        <lastName>Wójcik</lastName>
                        <email>katarzyna.wojcik@example.com</email>
                        <phone>+48 603 400 500</phone>
                        <latitude>51.1079</latitude>
                        <longitude>17.0385</longitude>
                    </user>
                    <user>
                        <id>5</id>
                        <firstName>Marek</firstName>
                        <lastName>Lewandowski</lastName>
                        <email>marek.lewandowski@example.com</email>
                        <phone>+48 604 500 600</phone>
                        <latitude>53.1325</latitude>
                        <longitude>23.1688</longitude>
                </users>
                """;


        Assertions.assertThatThrownBy(() -> xmlFileDeserializer.deserialize(xml))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The end-tag for element");

    }



}
