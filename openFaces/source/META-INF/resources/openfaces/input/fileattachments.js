/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

// ================================== END OF PUBLIC API METHODS
O$.FileAttachments = {
  _init:function (compId) {
    var fileAttachment = O$.initComponent(compId, null, {
    });

  },
  _fileUploadEndEventHandler:function (fileAttachmentsCompId, fileUploadCompId) {
    O$(fileUploadCompId).removeAllFiles();
    O$.Ajax._reload([fileAttachmentsCompId], {"executeRenderedComponents":true, "immediate":false});
  }

};
O$.DownloadAttachment = {
  _init: function(
          attachmentId,
          invokerId,
          eventHandlerName,
          fileAttachmentsComponentId,
          fileAttachmentId) {
    var downloadAttachment = O$.AttachmentUtils._init(attachmentId,
            invokerId,
            eventHandlerName,
            fileAttachmentsComponentId,
            fileAttachmentId);
    O$.extend(downloadAttachment, {
      _downloadForEvent:function (e) {
        downloadAttachment.invokerEvent = O$.getEvent(e);
        downloadAttachment._initDownload();
        return false;
      },

      _initDownload:function () {
        O$.setHiddenField(downloadAttachment, downloadAttachment._fileAttachmentsComponent.id + "::download", "true");
        O$.submitById(downloadAttachment.id);
        O$.setHiddenField(downloadAttachment, downloadAttachment._fileAttachmentsComponent.id + "::download", "false");
      },
      _addEventHandler:function (invoker) {
        if (invoker.of_removeAttachmentIds && O$.arrayContainsValue(invoker.of_removeAttachmentIds, downloadAttachment.id)) {
          var idx = invoker.of_removeAttachmentIds.indexOf(downloadAttachment.id);
          invoker.of_removeAttachmentIds.splice(idx - 1, 1);
        }
        if (!O$._removeAttachmentEventHandlers)
          O$._removeAttachmentEventHandlers = [];
        if (!O$._removeAttachmentEventHandlers[downloadAttachment.id]) {
          O$._removeAttachmentEventHandlers[downloadAttachment.id] = invoker[downloadAttachment._eventHandlerName];
        }

        invoker[downloadAttachment._eventHandlerName] = function (event) {
          downloadAttachment._downloadForEvent(event);
          return false;
        };

        if (!invoker.of_removeAttachmentIds)
          invoker.of_removeAttachmentIds = [];
        // we save ids of all registered confirmations to support use-case when Attachment is reloaded with a4j
        invoker.of_removeAttachmentIds.push(downloadAttachment.id);
      }
    });
    downloadAttachment._setListenerOnElement();
  }
};

O$.RemoveAttachment = {
  _init: function(
          attachmentId,
          invokerId,
          eventHandlerName,
          fileAttachmentsComponentId,
          fileAttachmentId) {
    var removeAttachment = O$.AttachmentUtils._init(attachmentId,
            invokerId,
            eventHandlerName,
            fileAttachmentsComponentId,
            fileAttachmentId);
    O$.extend(removeAttachment, {
      _removeForEvent:function (e) {
        removeAttachment.invokerEvent = O$.getEvent(e);
        removeAttachment._initRemove();
        return false;
      },
      _initRemove:function () {
        O$.Ajax.requestComponentPortions(removeAttachment._fileAttachmentsComponent.id, ["nothing"],
                JSON.stringify({callRemoveListener:true, attachmentId:removeAttachment._fileAttachmentId}),
                function (fileUpload, portionName, portionHTML, portionScripts, portionData) {
                  if (portionData['isCalled'] == "true") {
                    removeAttachment._reloadComponent();
                  }
                }, null, true);
      },
      _addEventHandler:function (invoker) {
        if (invoker.of_removeAttachmentIds && O$.arrayContainsValue(invoker.of_removeAttachmentIds, removeAttachment.id)) {
          var idx = invoker.of_removeAttachmentIds.indexOf(removeAttachment.id);
          invoker.of_removeAttachmentIds.splice(idx - 1, 1);
        }
        if (!O$._removeAttachmentEventHandlers)
          O$._removeAttachmentEventHandlers = [];
        if (!O$._removeAttachmentEventHandlers[removeAttachment.id]) {
          O$._removeAttachmentEventHandlers[removeAttachment.id] = invoker[removeAttachment._eventHandlerName];
        }

        invoker[removeAttachment._eventHandlerName] = function (event) {
          removeAttachment._removeForEvent(event);
          return false;
        };

        if (!invoker.of_removeAttachmentIds)
          invoker.of_removeAttachmentIds = [];
        // we save ids of all registered confirmations to support use-case when Attachment is reloaded with a4j
        invoker.of_removeAttachmentIds.push(removeAttachment.id);
      }

    });
    removeAttachment._setListenerOnElement();
  }
};

O$.AttachmentUtils = {
  _init: function(
          attachmentId,
          invokerId,
          eventHandlerName,
          fileAttachmentsComponentId,
          fileAttachmentId) {
    var attachment = O$.initComponent(attachmentId, null, {
      _invokerId:invokerId,
      _eventHandlerName:eventHandlerName ? eventHandlerName : "onclick",
      _fileAttachmentsComponent:O$(fileAttachmentsComponentId),
      _fileAttachmentId:fileAttachmentId,
      _setListenerOnElement:function () {
        if (attachment._invokerId && attachment._eventHandlerName) {
          var addAttachmentListener = function () {
            var invoker = O$(attachment._invokerId);
            if (!invoker) {
              var thisTime = new Date().getTime();
              if (!attachment._firstAttachAttemptTime) {
                attachment._firstAttachAttemptTime = thisTime;
                attachment._elapsedSinceFirstAttachAttempt = 0;
              } else {
                // don't take large chunks of javascript-busy time into account, count only execution-free time when timeouts flow reliably
                var elapsedSinceLastAttempt = thisTime - attachment._previousAttachAttemptTime;
                if (elapsedSinceLastAttempt < 100)
                  attachment._elapsedSinceFirstAttachAttempt += elapsedSinceLastAttempt;
              }
              if (attachment._elapsedSinceFirstAttachAttempt > 5000)
                throw "Invalid invokerId for attachment: " + attachment.id + ". Couldn't find component with clientId: " + attachment._invokerId;
              attachment._previousAttachAttemptTime = thisTime;
              setTimeout(addAttachmentListener, 30);
              return;
            }
            attachment._addEventHandler(invoker);

          };
          var invoker = O$(attachment._invokerId);
          if (invoker) { // this branch is to support loading Attachment through ajax/a4j
            addAttachmentListener();
          } else {
            O$.addLoadEvent(function () {
              addAttachmentListener();
            });
          }
        }
      },
      _reloadComponent:function () {
        O$.Ajax._reload([attachment._fileAttachmentsComponent.id], {"executeRenderedComponents":true, "immediate":false});
      }
    });
    return attachment;
  }
};