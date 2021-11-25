package com.tyron.javacompletion.options;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_IndexOptions extends IndexOptions {

  private final boolean shouldIndexPrivate;

  private final boolean shouldIndexMethodContent;

  private AutoValue_IndexOptions(
      boolean shouldIndexPrivate,
      boolean shouldIndexMethodContent) {
    this.shouldIndexPrivate = shouldIndexPrivate;
    this.shouldIndexMethodContent = shouldIndexMethodContent;
  }

  @Override
  public boolean shouldIndexPrivate() {
    return shouldIndexPrivate;
  }

  @Override
  public boolean shouldIndexMethodContent() {
    return shouldIndexMethodContent;
  }

  @Override
  public String toString() {
    return "IndexOptions{"
        + "shouldIndexPrivate=" + shouldIndexPrivate + ", "
        + "shouldIndexMethodContent=" + shouldIndexMethodContent
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof IndexOptions) {
      IndexOptions that = (IndexOptions) o;
      return this.shouldIndexPrivate == that.shouldIndexPrivate()
          && this.shouldIndexMethodContent == that.shouldIndexMethodContent();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= shouldIndexPrivate ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= shouldIndexMethodContent ? 1231 : 1237;
    return h$;
  }

  static final class Builder extends IndexOptions.Builder {
    private Boolean shouldIndexPrivate;
    private Boolean shouldIndexMethodContent;
    Builder() {
    }
    @Override
    public IndexOptions.Builder setShouldIndexPrivate(boolean shouldIndexPrivate) {
      this.shouldIndexPrivate = shouldIndexPrivate;
      return this;
    }
    @Override
    public IndexOptions.Builder setShouldIndexMethodContent(boolean shouldIndexMethodContent) {
      this.shouldIndexMethodContent = shouldIndexMethodContent;
      return this;
    }
    @Override
    public IndexOptions build() {
      if (this.shouldIndexPrivate == null
          || this.shouldIndexMethodContent == null) {
        StringBuilder missing = new StringBuilder();
        if (this.shouldIndexPrivate == null) {
          missing.append(" shouldIndexPrivate");
        }
        if (this.shouldIndexMethodContent == null) {
          missing.append(" shouldIndexMethodContent");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_IndexOptions(
          this.shouldIndexPrivate,
          this.shouldIndexMethodContent);
    }
  }

}
