// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: model.proto at 120:1
package com.tevinjeffrey.rutgersct.data.uctapi.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.squareup.wire.AndroidMessage;
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import okio.ByteString;

public final class UCTNotification
    extends AndroidMessage<UCTNotification, UCTNotification.Builder> {
  public static final ProtoAdapter<UCTNotification> ADAPTER = new ProtoAdapter_UCTNotification();

  public static final Parcelable.Creator<UCTNotification> CREATOR =
      AndroidMessage.newCreator(ADAPTER);
  public static final Long DEFAULT_NOTIFICATION_ID = 0L;
  public static final String DEFAULT_TOPIC_NAME = "";
  public static final String DEFAULT_STATUS = "";
  private static final long serialVersionUID = 0L;
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT64"
  )
  @Nullable
  public final Long notification_id;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  @Nullable
  public final String topic_name;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  @Nullable
  public final String status;

  @WireField(
      tag = 4,
      adapter = "com.tevinjeffrey.rutgersct.data.uctapi.model.University#ADAPTER"
  )
  @Nullable
  public final University university;

  public UCTNotification(
      @Nullable Long notification_id,
      @Nullable String topic_name,
      @Nullable String status,
      @Nullable University university) {
    this(notification_id, topic_name, status, university, ByteString.EMPTY);
  }

  public UCTNotification(
      @Nullable Long notification_id,
      @Nullable String topic_name,
      @Nullable String status,
      @Nullable University university,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.notification_id = notification_id;
    this.topic_name = topic_name;
    this.status = status;
    this.university = university;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof UCTNotification)) {
      return false;
    }
    UCTNotification o = (UCTNotification) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(notification_id, o.notification_id)
        && Internal.equals(topic_name, o.topic_name)
        && Internal.equals(status, o.status)
        && Internal.equals(university, o.university);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (notification_id != null ? notification_id.hashCode() : 0);
      result = result * 37 + (topic_name != null ? topic_name.hashCode() : 0);
      result = result * 37 + (status != null ? status.hashCode() : 0);
      result = result * 37 + (university != null ? university.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.notification_id = notification_id;
    builder.topic_name = topic_name;
    builder.status = status;
    builder.university = university;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (notification_id != null) {
      builder.append(", notification_id=").append(notification_id);
    }
    if (topic_name != null) {
      builder.append(", topic_name=").append(topic_name);
    }
    if (status != null) {
      builder.append(", status=").append(status);
    }
    if (university != null) {
      builder.append(", university=").append(university);
    }
    return builder.replace(0, 2, "UCTNotification{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<UCTNotification, Builder> {
    public Long notification_id;

    public String topic_name;

    public String status;

    public University university;

    public Builder() {
    }

    @Override
    public UCTNotification build() {
      return new UCTNotification(
          notification_id,
          topic_name,
          status,
          university,
          super.buildUnknownFields()
      );
    }

    public Builder notification_id(Long notification_id) {
      this.notification_id = notification_id;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Builder topic_name(String topic_name) {
      this.topic_name = topic_name;
      return this;
    }

    public Builder university(University university) {
      this.university = university;
      return this;
    }
  }

  private static final class ProtoAdapter_UCTNotification extends ProtoAdapter<UCTNotification> {
    ProtoAdapter_UCTNotification() {
      super(FieldEncoding.LENGTH_DELIMITED, UCTNotification.class);
    }

    @Override
    public UCTNotification decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1; ) {
        switch (tag) {
          case 1:
            builder.notification_id(ProtoAdapter.INT64.decode(reader));
            break;
          case 2:
            builder.topic_name(ProtoAdapter.STRING.decode(reader));
            break;
          case 3:
            builder.status(ProtoAdapter.STRING.decode(reader));
            break;
          case 4:
            builder.university(University.ADAPTER.decode(reader));
            break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public void encode(ProtoWriter writer, UCTNotification value) throws IOException {
      if (value.notification_id != null) {
        ProtoAdapter.INT64.encodeWithTag(writer, 1, value.notification_id);
      }
      if (value.topic_name != null) {
        ProtoAdapter.STRING.encodeWithTag(writer, 2, value.topic_name);
      }
      if (value.status != null) {
        ProtoAdapter.STRING.encodeWithTag(writer, 3, value.status);
      }
      if (value.university != null) {
        University.ADAPTER.encodeWithTag(writer, 4, value.university);
      }
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public int encodedSize(UCTNotification value) {
      return (value.notification_id != null ? ProtoAdapter.INT64.encodedSizeWithTag(
          1,
          value.notification_id
      ) : 0)
          + (value.topic_name != null ? ProtoAdapter.STRING.encodedSizeWithTag(2, value.topic_name)
                                      : 0)
          + (value.status != null ? ProtoAdapter.STRING.encodedSizeWithTag(3, value.status) : 0)
          + (value.university != null ? University.ADAPTER.encodedSizeWithTag(4, value.university)
                                      : 0)
          + value.unknownFields().size();
    }

    @Override
    public UCTNotification redact(UCTNotification value) {
      Builder builder = value.newBuilder();
      if (builder.university != null) {
        builder.university = University.ADAPTER.redact(builder.university);
      }
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
