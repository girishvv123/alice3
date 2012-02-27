package org.lgna.story.event;

public abstract class ViewEvent extends AbstractEvent {
	private final org.lgna.story.Entity entity;
	public ViewEvent( org.lgna.story.Entity entity ) {
		this.entity = entity;
	}
	public org.lgna.story.Entity getEntity() {
		return entity;
	}
}
