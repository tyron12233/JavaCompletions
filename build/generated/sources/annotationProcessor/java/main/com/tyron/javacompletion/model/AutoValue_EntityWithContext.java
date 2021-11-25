package com.tyron.javacompletion.model;

import javax.annotation.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_EntityWithContext extends EntityWithContext {

  private final Entity entity;

  private final SolvedTypeParameters solvedTypeParameters;

  private final int arrayLevel;

  private final boolean instanceContext;

  private AutoValue_EntityWithContext(
      Entity entity,
      SolvedTypeParameters solvedTypeParameters,
      int arrayLevel,
      boolean instanceContext) {
    this.entity = entity;
    this.solvedTypeParameters = solvedTypeParameters;
    this.arrayLevel = arrayLevel;
    this.instanceContext = instanceContext;
  }

  @Override
  public Entity getEntity() {
    return entity;
  }

  @Override
  public SolvedTypeParameters getSolvedTypeParameters() {
    return solvedTypeParameters;
  }

  @Override
  public int getArrayLevel() {
    return arrayLevel;
  }

  @Override
  public boolean isInstanceContext() {
    return instanceContext;
  }

  @Override
  public String toString() {
    return "EntityWithContext{"
        + "entity=" + entity + ", "
        + "solvedTypeParameters=" + solvedTypeParameters + ", "
        + "arrayLevel=" + arrayLevel + ", "
        + "instanceContext=" + instanceContext
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof EntityWithContext) {
      EntityWithContext that = (EntityWithContext) o;
      return this.entity.equals(that.getEntity())
          && this.solvedTypeParameters.equals(that.getSolvedTypeParameters())
          && this.arrayLevel == that.getArrayLevel()
          && this.instanceContext == that.isInstanceContext();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= entity.hashCode();
    h$ *= 1000003;
    h$ ^= solvedTypeParameters.hashCode();
    h$ *= 1000003;
    h$ ^= arrayLevel;
    h$ *= 1000003;
    h$ ^= instanceContext ? 1231 : 1237;
    return h$;
  }

  @Override
  public EntityWithContext.Builder toBuilder() {
    return new Builder(this);
  }

  static final class Builder extends EntityWithContext.Builder {
    private Entity entity;
    private SolvedTypeParameters solvedTypeParameters;
    private Integer arrayLevel;
    private Boolean instanceContext;
    Builder() {
    }
    private Builder(EntityWithContext source) {
      this.entity = source.getEntity();
      this.solvedTypeParameters = source.getSolvedTypeParameters();
      this.arrayLevel = source.getArrayLevel();
      this.instanceContext = source.isInstanceContext();
    }
    @Override
    public EntityWithContext.Builder setEntity(Entity entity) {
      if (entity == null) {
        throw new NullPointerException("Null entity");
      }
      this.entity = entity;
      return this;
    }
    @Override
    public EntityWithContext.Builder setSolvedTypeParameters(SolvedTypeParameters solvedTypeParameters) {
      if (solvedTypeParameters == null) {
        throw new NullPointerException("Null solvedTypeParameters");
      }
      this.solvedTypeParameters = solvedTypeParameters;
      return this;
    }
    @Override
    public EntityWithContext.Builder setArrayLevel(int arrayLevel) {
      this.arrayLevel = arrayLevel;
      return this;
    }
    @Override
    public int getArrayLevel() {
      if (arrayLevel == null) {
        throw new IllegalStateException("Property \"arrayLevel\" has not been set");
      }
      return arrayLevel;
    }
    @Override
    public EntityWithContext.Builder setInstanceContext(boolean instanceContext) {
      this.instanceContext = instanceContext;
      return this;
    }
    @Override
    public EntityWithContext build() {
      if (this.entity == null
          || this.solvedTypeParameters == null
          || this.arrayLevel == null
          || this.instanceContext == null) {
        StringBuilder missing = new StringBuilder();
        if (this.entity == null) {
          missing.append(" entity");
        }
        if (this.solvedTypeParameters == null) {
          missing.append(" solvedTypeParameters");
        }
        if (this.arrayLevel == null) {
          missing.append(" arrayLevel");
        }
        if (this.instanceContext == null) {
          missing.append(" instanceContext");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_EntityWithContext(
          this.entity,
          this.solvedTypeParameters,
          this.arrayLevel,
          this.instanceContext);
    }
  }

}
