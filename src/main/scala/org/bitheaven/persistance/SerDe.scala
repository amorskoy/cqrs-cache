package org.bitheaven.persistance

import org.bitheaven.Models.Event
import swaydb.data.slice.Slice
import swaydb.serializers.Serializer

/** SerDe represents Cache SerDe implemetation - SwayDB in this demo */
object SerDe {

    implicit object EventSerializer extends Serializer[Event] {
        override def write(ev: Event): Slice[Byte] = {
            Slice.create(256)
                .addLong(ev.id)
                .addLong(ev.event_ts)
                .addInt(ev.stage.length)
                .addString(ev.stage)
                .addInt(ev.dataset.length)
                .addString(ev.dataset)
                .close()
        }

        override def read(data: Slice[Byte]): Event = {
            val r = data.createReader()
            val id = r.readLong()
            val ts = r.readLong()
            val szStage = r.readInt()
            val stage = r.readString(szStage)
            val szDS = r.readInt()
            val ds = r.readString(szDS)

            Event(id, ts, stage, ds)
        }

    }

}
