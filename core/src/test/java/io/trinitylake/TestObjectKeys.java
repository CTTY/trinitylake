/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trinitylake;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.trinitylake.exception.InvalidArgumentException;
import io.trinitylake.models.LakehouseDef;
import org.junit.jupiter.api.Test;

public class TestObjectKeys {

  @Test
  public void testNamespaceKey() {
    LakehouseDef lakehouseDef =
        ObjectDefinitions.newLakehouseDefBuilder().setNamespaceNameMaxSizeBytes(8).build();
    assertThat(ObjectKeys.namespaceKey("ns1", lakehouseDef)).isEqualTo("B===ns1     ");
    assertThatThrownBy(() -> ObjectKeys.namespaceKey("", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("must be provided");
    assertThatThrownBy(() -> ObjectKeys.namespaceKey("aaaaaaaaa", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("must be less than or equal to 8");
  }

  @Test
  public void testIsNamespaceKey() {
    LakehouseDef lakehouseDef =
        ObjectDefinitions.newLakehouseDefBuilder().setNamespaceNameMaxSizeBytes(8).build();
    assertThat(ObjectKeys.isNamespaceKey("B===ns1     ", lakehouseDef)).isTrue();
    assertThat(ObjectKeys.isNamespaceKey("B===ns1  ", lakehouseDef)).isFalse();
    assertThat(ObjectKeys.isNamespaceKey("b===ns1", lakehouseDef)).isFalse();
  }

  @Test
  public void testNamespaceNameFromKey() {
    LakehouseDef lakehouseDef =
        ObjectDefinitions.newLakehouseDefBuilder().setNamespaceNameMaxSizeBytes(8).build();
    assertThat(ObjectKeys.namespaceNameFromKey("B===ns1     ", lakehouseDef)).isEqualTo("ns1");
    assertThatThrownBy(() -> ObjectKeys.namespaceNameFromKey("B===ns1  ", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("Invalid namespace key");
  }

  @Test
  public void testTableKey() {
    LakehouseDef lakehouseDef =
        ObjectDefinitions.newLakehouseDefBuilder()
            .setNamespaceNameMaxSizeBytes(8)
            .setTableNameMaxSizeBytes(8)
            .build();
    assertThat(ObjectKeys.tableKey("ns1", "t1", lakehouseDef)).isEqualTo("C===ns1     t1      ");
    assertThatThrownBy(() -> ObjectKeys.tableKey("", "t1", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("must be provided");
    assertThatThrownBy(() -> ObjectKeys.tableKey("ns1", "", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("must be provided");
    assertThatThrownBy(() -> ObjectKeys.tableKey("aaaaaaaaa", "t1", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("must be less than or equal to 8");
    assertThatThrownBy(() -> ObjectKeys.tableKey("ns1", "aaaaaaaaa", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("must be less than or equal to 8");
  }

  @Test
  public void testIsTableKey() {
    LakehouseDef lakehouseDef =
        ObjectDefinitions.newLakehouseDefBuilder()
            .setNamespaceNameMaxSizeBytes(8)
            .setTableNameMaxSizeBytes(8)
            .build();
    assertThat(ObjectKeys.isTableKey("C===ns1     t1      ", lakehouseDef)).isTrue();
    assertThat(ObjectKeys.isTableKey("C===ns1  t1   ", lakehouseDef)).isFalse();
    assertThat(ObjectKeys.isTableKey("c===ns1     t1      ", lakehouseDef)).isFalse();
  }

  @Test
  public void testTableNameFromKey() {
    LakehouseDef lakehouseDef =
        ObjectDefinitions.newLakehouseDefBuilder()
            .setNamespaceNameMaxSizeBytes(8)
            .setTableNameMaxSizeBytes(8)
            .build();
    assertThat(ObjectKeys.tableNameFromKey("C===ns1     t1      ", lakehouseDef)).isEqualTo("t1");
    assertThatThrownBy(() -> ObjectKeys.tableNameFromKey("B===ns1  ", lakehouseDef))
        .isInstanceOf(InvalidArgumentException.class)
        .hasMessageContaining("Invalid table key");
  }
}
