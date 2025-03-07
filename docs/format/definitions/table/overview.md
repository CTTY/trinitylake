# Overview

A table is a [tabular object](../overview.md#traits) that represents a collection of related data.

## Object Definition Schema

***Schema ID: 2***

| Field Name                 | Protobuf Type       | Description                                                                                                      | Required? | Default |
|----------------------------|---------------------|------------------------------------------------------------------------------------------------------------------|-----------|---------|
| name                       | string              | A user-friendly name of this table                                                                               | Yes       |         |
| schema                     | Schema              | Schema of the table, see [Table Schema](table-schema.md)                                                       | Yes       |         |
| distribution_keys          | repeated uint32     | The list of IDs for columns that are used as the distribution key                                                | No        |         |
| sort_keys                  | repeated uint32     | The list of IDs for columns that are used as sort key                                                            | No        |         |
| primary_key                | repeated uint32     | The list of IDs for columns that are used as primary key                                                         | No        |         |
| unique_columns             | repeated uint32     | The list of IDs for columns that are not used as primary key but are unique                                      | No        |         |
| table_type                 | string              | Table type, see [Table Type](table-type.md)                                                                    | No        | MANAGED |
| table_format               | string              | The format of the table, which decides the usage of `format_properties`. Currently `ICEBERG` is the only option. | Yes       |         |
| format_properties          | map<string, string> | Free form format-specific key-value string properties, e.g. [Apache Iceberg](./iceberg.md)                       | No        |         |
| properties                 | map<string, string> | Free form user-defined key-value string properties                                                               | No        |         |

## Name Size

All table names must obey the maximum size configuration defined in the [Lakehouse definition file](../lakehouse.md).