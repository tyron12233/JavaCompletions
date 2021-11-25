package com.tyron.javacompletion.completion;

import com.google.common.collect.ImmutableSet;
import com.tyron.javacompletion.model.Entity;
import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_ClassMemberCompletor_Options extends ClassMemberCompletor.Options {

  private final boolean includeAllMethodOverloads;

  private final boolean addBothInstanceAndStaticMembers;

  private final ImmutableSet<Entity.Kind> allowedKinds;

  private AutoValue_ClassMemberCompletor_Options(
      boolean includeAllMethodOverloads,
      boolean addBothInstanceAndStaticMembers,
      ImmutableSet<Entity.Kind> allowedKinds) {
    this.includeAllMethodOverloads = includeAllMethodOverloads;
    this.addBothInstanceAndStaticMembers = addBothInstanceAndStaticMembers;
    this.allowedKinds = allowedKinds;
  }

  @Override
  boolean includeAllMethodOverloads() {
    return includeAllMethodOverloads;
  }

  @Override
  boolean addBothInstanceAndStaticMembers() {
    return addBothInstanceAndStaticMembers;
  }

  @Override
  ImmutableSet<Entity.Kind> allowedKinds() {
    return allowedKinds;
  }

  @Override
  public String toString() {
    return "Options{"
        + "includeAllMethodOverloads=" + includeAllMethodOverloads + ", "
        + "addBothInstanceAndStaticMembers=" + addBothInstanceAndStaticMembers + ", "
        + "allowedKinds=" + allowedKinds
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ClassMemberCompletor.Options) {
      ClassMemberCompletor.Options that = (ClassMemberCompletor.Options) o;
      return this.includeAllMethodOverloads == that.includeAllMethodOverloads()
          && this.addBothInstanceAndStaticMembers == that.addBothInstanceAndStaticMembers()
          && this.allowedKinds.equals(that.allowedKinds());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= includeAllMethodOverloads ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= addBothInstanceAndStaticMembers ? 1231 : 1237;
    h$ *= 1000003;
    h$ ^= allowedKinds.hashCode();
    return h$;
  }

  static final class Builder extends ClassMemberCompletor.Options.Builder {
    private Boolean includeAllMethodOverloads;
    private Boolean addBothInstanceAndStaticMembers;
    private ImmutableSet<Entity.Kind> allowedKinds;
    Builder() {
    }
    @Override
    ClassMemberCompletor.Options.Builder includeAllMethodOverloads(boolean includeAllMethodOverloads) {
      this.includeAllMethodOverloads = includeAllMethodOverloads;
      return this;
    }
    @Override
    ClassMemberCompletor.Options.Builder addBothInstanceAndStaticMembers(boolean addBothInstanceAndStaticMembers) {
      this.addBothInstanceAndStaticMembers = addBothInstanceAndStaticMembers;
      return this;
    }
    @Override
    ClassMemberCompletor.Options.Builder allowedKinds(ImmutableSet<Entity.Kind> allowedKinds) {
      if (allowedKinds == null) {
        throw new NullPointerException("Null allowedKinds");
      }
      this.allowedKinds = allowedKinds;
      return this;
    }
    @Override
    ClassMemberCompletor.Options build() {
      if (this.includeAllMethodOverloads == null
          || this.addBothInstanceAndStaticMembers == null
          || this.allowedKinds == null) {
        StringBuilder missing = new StringBuilder();
        if (this.includeAllMethodOverloads == null) {
          missing.append(" includeAllMethodOverloads");
        }
        if (this.addBothInstanceAndStaticMembers == null) {
          missing.append(" addBothInstanceAndStaticMembers");
        }
        if (this.allowedKinds == null) {
          missing.append(" allowedKinds");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ClassMemberCompletor_Options(
          this.includeAllMethodOverloads,
          this.addBothInstanceAndStaticMembers,
          this.allowedKinds);
    }
  }

}
